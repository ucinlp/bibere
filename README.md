bibere
======

This is a library to maintain a database of publications currently backed by YAML files. The main purpose is to support static Github pages rendered without _any_ local computation, i.e. relying on Github-supported Jekyll to render webpages on the server.

We use bibere to maintain [our own publications](https://ucinlp.github.io/publications.html).

Here are the goals of this project:
- To be extremely easy to add/edit papers, such as quickly changing a YAML file.
- Automatic generation of HTML elements with some interactivity, BibTex entries (and `bib` file), etc.
- Use only Github Pages rendering (using Jekyll), i.e. don't use features of Jekyll not supported by Github, or any other offline processing.
- Extensible and customizable.

Current set of features:
- Store authors, papers, and venues in human-editable YAML files
- Jekyll generator: render papers using the Liquid template engine to generate classed-up html, that can be beautified using CSS.
- BibTex generator: writes out publications as @inproceedings with the basic fields (as Jekyll include), also generates `bib` files as needed.

## Quick Start

### Minimal Example

A bare-bones version of a GithubPages website with papers rendered with `bibere` is [available here](http://ucinlp.github.io/bibere).

Browse the source code of the site at [ucinlp/bibere/gh-pages](https://github.com/ucinlp/bibere/tree/gh-pages).

### Requirements

- Github Pages hosted website using Jekyll
- [JQuery](https://code.jquery.com/) included in the `<head>`. `bibere` uses it to slide paper details. See [example](https://github.com/ucinlp/bibere/blob/gh-pages/_includes/head.html#L11) of how to include it.

### Getting Started

`bibere` requires files in the following two directories in your Github Pages project:
- `_data/bibere` to store the papers, authors, and venues as YAML files, see example here: https://github.com/ucinlp/bibere/tree/master/_data
- `_includes/bibere` containing the package files from here: https://github.com/ucinlp/bibere/tree/master/_includes
- **Recommended**: You might want to use a default style file, such as [this one](https://github.com/ucinlp/bibere/blob/gh-pages/_sass/bibere.scss), to make sure your publications don't look wonky.

Once you have the file, edit any Markdown or HTML file and insert the following:
```liquid
{% include bibere/byyear.html %}
```

And you will get a list of papers!

See these examples:
- From demo site: [webpage](https://ucinlp.github.io/bibere/pubs/) ([source](https://github.com/ucinlp/bibere/blob/gh-pages/pubs.md))
- Our own papers: [webpage](https://ucinlp.github.io/publications.html), ([source](https://github.com/ucinlp/ucinlp.github.io/blob/master/publications.html))

## Data Files to Store Papers

Here are the description of the three files that will store the meta data about your papers.

For actual examples of these files, see versions of this:
- on the [demo site](https://github.com/ucinlp/bibere/tree/gh-pages/_data/bibere)
- on the [group site](https://github.com/ucinlp/ucinlp.github.io/tree/master/_data/bibere/) (around 120 papers)


### Papers in `papers.yml`

This is the _only_ necessary file. It lists all of the papers as a _dictionary_ for `bibere` to render.

Here is the minimal example for a paper:
```yaml
thumbs:emnlp02:
  title: Thumbs up? Sentiment Classification using Machine Learning Techniques
  venue: Empirical Methods in Natural Language Processing (EMNLP)
  authors:
    - Bo Pang
    - Lillian Lee
    - Shivakumar Vaithyanathan
  type: Conference
  year: 2002
```
Which should give you something like this:
![image](https://user-images.githubusercontent.com/585359/118951911-5749c600-b910-11eb-9ecb-630500fd6511.png)

Here is the version of the paper if the author and venue metadata is stored separately:
```yaml
thumbs:emnlp02:
  title: Thumbs up? Sentiment Classification using Machine Learning Techniques
  venue: emnlp
  type: Conference
  authors:
    - bo
    - lillian
    - shivakumar
  year: 2002
```
We currently support many more fields for papers. Here is a much more detailed version of the a paper entry:
```yaml
checklist:acl20:
  title: >
    Beyond Accuracy: Behavioral Testing of NLP models with CheckList
  venue: "acl"
  year: 2020
  type: "Conference"
  authors:
    - "marco"
    - "Tongshuang Wu"
    - "carlos"
    - "sameer"
  pages: 4902-4912
  links:
    - name: "PDF"
      link: "/files/papers/checklist-acl20.pdf"
    - name: "Code"
      link: "https://github.com/marcotcr/checklist"
    - name: "ACL Anthology"
      link: "https://www.aclweb.org/anthology/2020.acl-main.442/"
    - name: "Video+Slides"
      link: "https://slideslive.com/38929272"
    - name: "ArXiV"
      link: "https://arxiv.org/abs/2005.04118"
  abstract: >
    Although measuring held-out accuracy has been the primary approach to evaluate generalization, it often overestimates the performance of NLP models, while alternative approaches for evaluating models either focus on individual tasks or on specific behaviors. Inspired by principles of behavioral testing in software engineering, we introduce CheckList, a task-agnostic methodology for testing NLP models. CheckList includes a matrix of general linguistic capabilities and test types that facilitate comprehensive test ideation, as well as a software tool to generate a large and diverse number of test cases quickly. We illustrate the utility of CheckList with tests for three tasks, identifying critical failures in both commercial and state-of-art models. In a user study, a team responsible for a commercial sentiment analysis model found new and actionable bugs in an extensively tested model. In another user study, NLP practitioners with CheckList created twice as many tests, and found almost three times as many bugs as users without it.
  emphasis: "Best Paper Award"
  note: "To be presented at XYZ Workshop"
  ```

### Authors in `authors.yaml`

In `papers.yml`, you can just list the authors by name. However, if you want to use a short name to refer to the same authors repeatedly, or you want to attach metadata to the authors (such as their website), you'll have to use `authors.yml` file.

This file is also a _dictionary_, with the short names for the authors as keys:
```yaml
lillian:
  name:
    first: Lillian
    last: Lee
  website: "https://www.cs.cornell.edu/home/llee/"
```
Website is optional.

### Venues in `venues.yaml`

As with authors, `papers.yml` can directly mention venues in the `venue:` field. However, sometimes you want to reuse the names to save effort and avoid typos.

The `venues.yml` file helps you with that, by allowing you to add short venue names as keys:
```yaml
emnlp:
  name: "Empirical Methods in Natural Language Processing (EMNLP)"
```
This is pretty minimal for now, but we might add more fields later, such as putting the venue type here rather than in `papers.yml`.

## Rendering Papers

There are a number of Jekyll commands in order to render the papers in HTML/Markdown or BibTex files.

### List of publications, by year

This command lists all the papers, sorted by year (reverse chronological for now).
```liquid
{% include bibere/byyear.html %}
```
There are additional fields for this command:
- `mainAuthor`: author ID for the name that you want to highlight, e.g. if you're listing the papers on your personal website.
- `filterAuthor`: author ID of the author whose papers you want to include, e.g. you want to create an author-specific list of papers.
- `minYear`: Papers whose `year` field is same or later than this year will be included.

Here is an example that uses all of these (lists papers published >=2016 written by `bo`, but highlight `lillian` in the entries):
```liquid
{% include bibere/byyear.html mainAuthor="lillian" filterAuthor="bo" minYear=2016 %}
```

### Rendering a Single Paper

You might want to just render a single paper, such as in a list of _selected publications_ or such.
```liquid
{% include bibere/paper.html pid="thumbs:emnlp02" %}
```
Also supports `mainAuthor` for highlighting a single author.

Note that this is an `<li>` element, so best to surround with `<ul>` or `<ol>`.

### Generating a BibTex bibliography

If you include the following snippet in the Markdown file to be processed by Jekyll, it will generate a download link, which will let you download the `bib` file containing all the papers.
```html
<a id="download_bib" download="papers.bib" href=”” >BibTex file!</a>

<script>
    var bib = `
    {%- include bibere/bibtex-all.html -%}
    `;

    var data = new Blob([bib], {type: 'text/plain'});
    var url = window.URL.createObjectURL(data);
    document.getElementById('download_bib').href = url;
</script>
```
The command `bibere/bibtex-all.html` supports the same fields as `byyear`, i.e. 
- `mainAuthor`, if you want `\textbf` in the `bib` file for the main author, such as for use on CV, research statements, etc.
- `filterAuthor`, if you want to include a subset of the papers
- `minYear`, include only papers publish in, or after, this year

## Styling

For a _reasonable_ initial style, you might want to start with something like the following [basic style file](https://github.com/ucinlp/bibere/blob/gh-pages/_sass/bibere.scss).

## Python tools (under development)

Some features are implemented in Python:
- Word document format of publications (requires `docx` module in Python2, or `python-docx` in Python3)
- Extract first page of all papers (requires `pypdftk` module)

_More documentation coming soon_
