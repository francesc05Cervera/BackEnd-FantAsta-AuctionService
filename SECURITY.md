# Security status

AuctionService remains on Spring Framework 6.2.19, the Spring Boot 3.5.16 managed version. Maven Central does not publish the previously considered 6.2.20 release, while Trivy identifies Spring Framework 7.0.9 as the available fixed version. The two CVEs therefore remain visible to Trivy and keep the dependency-audit job failing until a compatible upstream patch is released or a separately validated Spring Boot 4 migration is approved.

`CVE-2026-47884` requires `XsltView` combined with a broad `/**` view mapping and an implicit view name. This REST-only service has no `XsltView`, no server-side view rendering, and no broad catch-all controller mapping. `CVE-2026-47890` requires Server-Sent Events with view fragments; this service has no SSE endpoints or view fragments.
