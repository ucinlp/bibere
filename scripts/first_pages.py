#!/usr/bin/python3
import argparse
from read_json import *
import tempfile
import shutil
import pypdftk
import os

def get_pdf(source, dest):
    shutil.copy(source, dest)

def run(idir, bdir, ofile):
    authors, venues, papers = read_all_info(idir)

    fpdf_names = []
    tmpdirname = tempfile.mkdtemp()
    for p in papers:
        if p['pubTypeSlot'] == 'Conference' or p['pubTypeSlot'] == 'Journal':
            if 'pdfLink' not in p:
                print("pdfLink missing:", p['id'])
            elif p['pdfLink'].startswith("http"):
                print("local link missing:", p['id'])
            else:
                source = bdir + "/" + p['pdfLink']
                i = len(fpdf_names)
                dest = "%s/%d.pdf" % (tmpdirname, i)
                print("getting %s, putting it %s" % (source, dest))
                get_pdf(source, dest)
                tdir = "%s/%d/" % (tmpdirname, i)
                os.mkdir(tdir)
                fpdf_names.append(tdir + "page_01.pdf")
                pypdftk.split(dest, tdir)
    pypdftk.concat(fpdf_names, out_file=ofile)
    shutil.rmtree(tmpdirname)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input", help="directory containing the json files for authors/papers", required=True)
    parser.add_argument("-b", "--basedir", help="the base directory of where the full PDFs reside.", required=True)
    parser.add_argument("-o", "--output", help="output pdf file for the first pages", required=True)
    args = parser.parse_args()
    print("input:  ", args.input)
    print("basedir:  ", args.basedir)
    print("output: ", args.output)
    run(args.input, args.basedir, args.output)
