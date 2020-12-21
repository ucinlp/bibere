#!/usr/bin/python3
import argparse
from read_json import *
import os

def output_author(author, ofile):
    # print(author['id'])
    ofile.write("%s:\n" % (author['id']))
    ofile.write("  name:\n")
    ofile.write("    first: \"%s\"\n" % (author['name']['first']))
    ofile.write("    last: \"%s\"\n" % (author['name']['last']))
    if "website" in author:
        ofile.write("  website: \"%s\"\n" % (author['website']))
    ofile.write("\n")

def output_venue(venue, ofile):
    # print(venue['id'])
    ofile.write("%s:\n" % (venue['id']))
    ofile.write("  name: \"%s\"\n" % (venue['name']))
    ofile.write("\n")

def output_paper(paper, ofile):
    # print(paper['id'])
    ofile.write("%s:\n" % (paper['id']))
    # mandatory fields
    ofile.write("  title: >\n    %s\n" % (paper['title']))
    ofile.write("  venue: \"%s\"\n" % (paper['venueId']))
    ofile.write("  year: %s\n" % (paper['year']))
    ofile.write("  type: \"%s\"\n" % (paper['pubTypeSlot']))
    ofile.write("  authors:\n")
    for aid in paper['authorIds']:
        ofile.write("    - \"%s\"\n" % aid)
    # optional
    # links
    if "pdfLink" in paper or "extraLinksSlot" in paper:
        ofile.write("  links:\n")
        if "pdfLink" in paper:
            ofile.write("    - name: \"%s\"\n" % "PDF")
            ofile.write("      link: \"%s\"\n" % paper['pdfLink'])
        if "extraLinksSlot" in paper:
            for k, v in paper['extraLinksSlot']:
                ofile.write("    - name: \"%s\"\n" % k)
                ofile.write("      link: \"%s\"\n" % v)
    # abstract
    if "abstractText" in paper:
        ofile.write("  abstract: >\n    %s\n" % (paper['abstractText'].encode('utf-8').decode()))
    # notes
    if "emphasisNote" in paper:
        ofile.write("  emphasis: \"%s\"\n" % (paper['emphasisNote']))
    if "note" in paper:
        ofile.write("  note: \"%s\"\n" % (paper['note']))
    # other tags
    if "tagsSlot" in paper:
        ofile.write("  extraTags: [ ")
        ofile.write(", ".join(['"' + tag + '"' for tag in paper['tagsSlot']]))
        ofile.write(" ]\n")
    # extra fields
    if "extraFieldsSlot" in paper:
        ofile.write("  bibtex_fields:\n")
        for k,v in paper['extraFieldsSlot']:
            ofile.write("    %s: \"%s\"\n" % (k, v))
    # weight
    if "weight" in paper:
        ofile.write("  sort_weight: %s\n" % (str(paper['weight'])))
    ofile.write("\n")

def output_authors(authors, odir):
    print("Printing all authors")
    afile = odir + "/authors.yml"
    with open(afile, 'w') as ofile:
        for author in authors:
            output_author(author, ofile)
    print("Done writing authors")

def output_venues(venues, odir):
    print("Printing all venues")
    vfile = odir + "/venues.yml"
    with open(vfile, 'w') as ofile:
        for venue in venues:
            output_venue(venue, ofile)

def output_papers(papers, odir):
    print("Printing all papers")
    pfile = odir + "/papers.yml"
    with open(pfile, 'w') as ofile:
        for paper in papers:
            output_paper(paper, ofile)

def run(idir, odir):
    pfile = idir + "/papers.json"
    afile = idir + "/authors.json"
    vfile = idir + "/venues.json"
    authors = read_json(afile)
    venues = read_json(vfile)
    papers = read_json(pfile)
    assert(os.path.isdir(odir), odir + " should be a directory")
    output_authors(authors, odir)
    output_venues(venues, odir)
    output_papers(papers, odir)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input", help="directory containing the json files for authors/papers", required=True)
    parser.add_argument("-o", "--output", help="directory where all the files will be written", required=True)
    args = parser.parse_args()
    print("input:  ", args.input)
    print("output: ", args.output)
    run(args.input, args.output)
