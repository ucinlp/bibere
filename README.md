bibere
======

This is a library to maintain a database of publications currently backed by YAML files. The main purpose is to support Github pages, with additional features for BibTex for citations/resumes, raw HTML for webpages, Markdown for whatever people use Markdown for, etc.

Current set of features:
- Read and write authors, papers, and venues in human-editable YAML files
- Jekyll generator: render papers using the Liquid template engine to generate classed-up html, that can be beautified using CSS.
- BibTex generator: writes out publications as @inproceedings with the basic fields (as Jekyll include)

## Python tools

Some of recent features are implemented in Python:
- Word document format of publications (requires `docx` module in Python2, or `python-docx` in Python3)
- Extract first page of all papers (requires `pypdftk` module)

## Key Concepts

Data is stored as three YAML files: authors, venues, and papers. Authors and venues are simple lists of names and websites etc. attached to a unique id. The papers file is the most complex, with many possible fields.

_More documentation coming soon_
