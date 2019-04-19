# githubinfo

A tiny Clojure/ClojureScript study.

Caches GitHub repo statistics to local database.

# Usage

Init database
```
$ lein run migrate
```

Start up
```
$ lein run
```

Start for development
```
# 1st console:
$ lein repl
user=> (start)

# 2nd console:
$ lein cljsbuild auto
```


## UI

Open UI: http://localhost:3000 (work in progress...)

## REST API

See orgs/users which repos are cached: http://localhost:3000/api

See stats of repos of org/user: http://localhost:3000/api?org=nojanen
(the stats will be downloaded if not in cache)
