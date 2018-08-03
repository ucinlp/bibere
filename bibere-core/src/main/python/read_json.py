import json

def read_authors(afile):
    authors = dict()
    with open(afile) as data_file:
        data = json.load(data_file)
        for a in data:
            authors[a['id']] = a['name']['first'] + ' ' + a['name']['last']
    return authors

def read_venues(vfile):
    venues = dict()
    with open(vfile) as data_file:
        data = json.load(data_file)
        for v in data:
            venues[v['id']] = v['name']
    return venues

def read_papers(pfile):
    papers = []
    with open(pfile) as data_file:
        papers = json.load(data_file)
    return papers

def read_all_info(dir):
    pfile = dir + "/papers.json"
    afile = dir + "/authors.json"
    vfile = dir + "/venues.json"
    authors = read_authors(afile)
    venues = read_venues(vfile)
    papers = read_papers(pfile)
    return authors, venues, papers
