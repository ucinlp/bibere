---
layout: page
title: All Papers
permalink: /pubs/
---

You can generate the list of all papers by including:

```liquid
{% raw %}{% include bibere/byyear.html %}{% endraw %} 
```

You can use `mainAuthor`, `filterAuthor`, and `minYear` to highlight an author, filter by an author, and only show _latest_ papers, respectively.

For more details, see [this page](https://github.com/ucinlp/bibere/tree/master#list-of-publications-by-year).



<div markdown="0">
{% include bibere/byyear.html %}
</div>