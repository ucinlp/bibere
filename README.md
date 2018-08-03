bibere
======

This is a library to maintain a database of publications, currently backed by json files. The main purpose is to allow output to various formats: BibTex for citations/resumes, HTML for webpages, Markdown for whatever people use Markdown for, and Json for everything else.

Current set of features:
- Read and write authors, papers, and venues in human-editable json files
- HTML generator: write out publications in a classed-up html, that can be beautified using CSS.
- BibTex generator: writes out publications as @inproceedings with the basic fields

Coming soon:
- BibTex: support more publication formats
- Markdown/Text generator
- make initialization easier using existing BibTex

## Python tools

Some of recent features are implemented in Python:
- Word document format of publications (requires `docx` module in Python2, or `python-docx` in Python3)
- Extract first page of all papers (requires `pypdftk` module)

## Initializing Publications

### From Json Files

For examples, see '''bibere-examples'''.

#### Authors

#### Venues

#### Papers

### From an existing BibTex file

## HTML Generator

### Basic Usage

### Advanced Customizations

## BibTex Generator

### Basic Usage

### Advanced Customizations
