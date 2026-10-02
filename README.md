# ktor-allowlist-plugin

[ktor](https://ktor.io) plugin for adding [Connection-Allowlist](https://wicg.github.io/connection-allowlists) headers to responses.

### Usage

```kotlin
install(ConnectionAllowlist) {
    reportOnly = false
    includeResponseOrigin = true
    allowedUrlPatterns = setOf("https://whatever.example.com")
    reportTo = "https://reporting.example.com"
}
```
