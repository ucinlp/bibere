---
# Feel free to add content and custom Front Matter to this file.
# To modify the layout, see https://jekyllrb.com/docs/themes/#overriding-theme-defaults

layout: home
---

This is a demo site for the bibere project. We will include some examples of rendering papers here, but for more details/context, read the <a href="/bibere/about/">About</a> page.

# All papers

For the list of all papers, see [all papers](/bibere/pubs) page.

# Single paper

A single paper can be included using:
`{% raw %}{% include bibere/paper.html pid="thumbs:emnlp02" %}{% endraw %}`

<ul>
{% include bibere/paper.html pid="thumbs:emnlp02" %}
</ul>

# BibTex

## BibTex file

A file containing all the bib entries can be generated using the command [provided here](https://github.com/ucinlp/bibere/tree/master#generating-a-bibtex-bibliography).

<div markdown="0">
Download the (automatically generated) <a id="download_bib" download="papers.bib" href="" >example BibTex file</a> here.
</div>

You can use `mainAuthor`, `filterAuthor`, and `minYear`.

<script>
    var bib = `
    {%- include bibere/bibtex-all.html -%}
    `;

    var data = new Blob([bib], {type: 'text/plain'});
    var url = window.URL.createObjectURL(data);
    document.getElementById('download_bib').href = url;
</script>

## BibTex entries

You can also generated single entries using:
```liquid
{% raw %}{%- include bibere/bibtex.html pid="thumbs:emnlp02" -%}{% endraw %} 
```

which gives you this:
```bibtex
{% include bibere/bibtex.html pid="thumbs:emnlp02" %}
```
