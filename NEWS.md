# History of skyscraper-cache-mapdb releases

## 0.1.1 (2015-12-17)

- `*print-length*` and `*print-level*` are now rebound to `nil` upon
  serialization, causing it to not break when you have these variables
  set in the REPL.
- New functions `export-values` and `import-values`.

## 0.1.0 (2015-10-03)

- First public release.
