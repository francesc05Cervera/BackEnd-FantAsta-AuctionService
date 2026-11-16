# Security status

The current PR keeps AuctionService on Spring Boot 3.5.16 / Spring Framework 6.2.19 to preserve the existing REST/JPA/Jakarta contract.

The final Trivy audit reports two critical Spring WebMVC issues: `CVE-2026-47884` and `CVE-2026-47890`. Trivy reports the fixed line as Spring Framework 7.0.9. Moving to that line requires a Spring Boot 4 migration and compatibility testing of the REST stack, JPA, security filters, and Render image; it is not silently introduced in this PR.

Until that migration is separately approved and tested, this PR must not be considered production-ready. Keep the service behind the intended gateway/network boundary and do not merge while the CI audit remains red.
