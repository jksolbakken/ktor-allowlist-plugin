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

Parameters:

| Name                    | Description                                               | Default value |
| ------                  | -----                                                     | -------       |
| `reportOnly`            | Do not block, just report                                 | false         |
| `includeResponseOrigin` | Allow connections to the origin where this page is served | true          |
| `allowedUrlPatterns`    | Additional allowed origins                                | empty         |
| `reportTo`              | Where to report violations                                | empty         |
