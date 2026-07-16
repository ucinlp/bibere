#!/usr/bin/env python3
"""
Generate the "Co-Authors, sorted by number of papers" list from bibere data.

Counts, for every co-author, the number of papers they share with the primary
author, keeps those with at least --min papers, sorts by count (descending,
ties broken alphabetically) and emits the <ul> used on the group page. Author
websites are pulled from authors.yml (works whether a paper references an
author by their short key or by an inline full name).

Usage:
    # print the HTML
    python scripts/coauthors.py

    # rewrite the list in place on the site's group page
    python scripts/coauthors.py --update /path/to/sameersingh.github.com/group.html

Options:
    --data DIR    bibere _data directory (default: ../_data next to this script)
    --min N       minimum shared papers to be listed (default: 3)
    --exclude X   primary author to exclude (default: "Sameer Singh")
"""
import argparse
import os
import re
import sys
from collections import Counter

try:
    import yaml
except ImportError:
    sys.exit("PyYAML is required: pip install pyyaml")

MARKER = "Co-Authors, sorted by number of papers"


def build(data_dir, min_papers, exclude):
    authors = yaml.safe_load(open(os.path.join(data_dir, "authors.yml")))
    papers = yaml.safe_load(open(os.path.join(data_dir, "papers.yml")))

    key_name = {k: f"{v['name']['first']} {v['name']['last']}" for k, v in authors.items()}
    name_web = {
        f"{v['name']['first']} {v['name']['last']}": v["website"]
        for v in authors.values()
        if v.get("website")
    }

    counts = Counter()
    for p in papers.values():
        for a in (p.get("authors") or []):
            name = key_name.get(a, a).strip()   # resolve key -> name, else inline string
            if name.lower() == exclude.lower():
                continue
            counts[name] += 1

    coas = sorted(
        [(c, n) for n, c in counts.items() if c >= min_papers],
        key=lambda x: (-x[0], x[1]),
    )

    lines = ['<ul class="list-unstyled row">']
    for _, name in coas:
        web = name_web.get(name)
        inner = f'<a href="{web}">{name}</a>' if web else name
        lines += [
            '  <li class="col-lg-3 col-md-4 col-sm-6">',
            f'    <span class="author">{inner}</span>',
            '  </li>',
            '',
        ]
    if lines[-1] == "":
        lines.pop()
    lines.append("</ul>")
    return "\n".join(lines), len(coas)


def update_file(path, new_ul):
    html = open(path).read()
    if MARKER not in html:
        sys.exit(f"Could not find the '{MARKER}' section in {path}")
    i = html.index(MARKER)
    start = html.index('<ul class="list-unstyled row">', i)
    end = html.index("</ul>", start) + len("</ul>")
    open(path, "w").write(html[:start] + new_ul + html[end:])


def main():
    here = os.path.dirname(os.path.abspath(__file__))
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--data", default=os.path.join(here, "..", "_data"))
    ap.add_argument("--min", type=int, default=3)
    ap.add_argument("--exclude", default="Sameer Singh")
    ap.add_argument("--update", help="group.html file to rewrite in place")
    args = ap.parse_args()

    new_ul, n = build(args.data, args.min, args.exclude)
    if args.update:
        update_file(args.update, new_ul)
        print(f"Updated {args.update} with {n} co-authors (>= {args.min} papers).", file=sys.stderr)
    else:
        print(new_ul)


if __name__ == "__main__":
    main()
