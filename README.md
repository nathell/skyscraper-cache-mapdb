# skyscraper-cache-mapdb

## About

This is a cache backend for [Skyscraper] that stores the downloaded/scraped data in a [MapDB] database.

 [Skyscraper]: https://github.com/nathell/skyscraper/
 [MapDB]: http://www.mapdb.org/

## Why?

Skyscraper’s default backends are simple and work well when the number of pages that Skyscraper needs to visit is moderate. However, in the case where it grows to hundreds of thousands of pages, Skyscraper will create a separate file (or even two files) for each of them. This can impose a significant burden on the filesystem. Hence the need to store the data in a key-value store instead.

MapDB was chosen because it’s pure JVM and can work off-heap. The backend is opinionated and the API is kept as simple as possible: for example, data are kept in a MapDB tree map and there’s currently no way to change this.

I’ve factored out this backend to a separate project to keep Skyscraper core dependencies to the minimum.

**Caveat:** this backend uses MapDB 2.0.x, which is currently in beta. No guarantees are made. Use at your own risk.

## How?

Require `skyscraper.cache.mapdb`:

    (ns your-app
      (:require [skyscraper :refer :all]
                [skyscraper.cache.mapdb :as mapdb)))

Instantiate the backend:

    (def cache (mapdb/mapdb-cache "/home/you/skyscraper-cache.db"))

Then tell Skyscraper to use it, like this:

    (scrape (seed) :html-cache cache :processed-cache cache)

Skyscraper sets up the backend to automatically close when you quit the JVM, but you may also close it manually:

    (mapdb/close cache)

## License

Copyright (C) 2015 Daniel Janus, http://danieljanus.pl

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
