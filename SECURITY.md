# Security status

AuctionService uses Spring Framework 6.2.20, the compatible patched release for `CVE-2026-47884` and `CVE-2026-47890`. Spring Framework's official 6.2.20 changelog lists both fixes.

Some vulnerability feeds list only Spring Framework 7.0.9 as a patched version. The CI suppression is limited to these two CVEs and exists solely to account for that advisory-version mismatch; it does not disable Trivy or lower its severity threshold. It can be removed once the scanner database recognizes 6.2.20 as patched.

`CVE-2026-47884` requires `XsltView` combined with a broad `/**` view mapping and an implicit view name. This REST-only service has no `XsltView`, no server-side view rendering, and no broad catch-all controller mapping. `CVE-2026-47890` requires Server-Sent Events with view fragments; this service has no SSE endpoints or view fragments.
