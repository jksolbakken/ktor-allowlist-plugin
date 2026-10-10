package no.jksolbakken

import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.response.header
import java.net.URL

/*
 * Plugin for adding Connection Allowlist headers in Ktor
 */
val ConnectionAllowlist = createApplicationPlugin(
    name = "ConnectionAllowlist", //
    createConfiguration = ::PluginConfiguration
) {
    onCall { call ->
        val allowlistHeaderName = if (pluginConfig.reportOnly)
            "Connection-Allowlist-Report-Only" else "Connection-Allowlist"
        val reportingEndpointsHeaderName = "Reporting-Endpoints"

        val responseOrigin = if (pluginConfig.includeResponseOrigin) "response-origin" else ""
        val otherOrigins = pluginConfig.allowedUrlPatterns.joinToString(" ") {
            """"$it""""
        }.let {
            if (pluginConfig.includeResponseOrigin && it.isNotEmpty()) " $it" else it
        }

        val caHeaderValue = buildString {
            append("($responseOrigin$otherOrigins)")
            pluginConfig.reportTo?.also {
                append("; report-to=default")
            }
        }

        pluginConfig.reportTo?.also { reportTo ->
            call.response.header(reportingEndpointsHeaderName, """default="$reportTo"""")
        }

        call.response.header(allowlistHeaderName, caHeaderValue)
    }
}

class PluginConfiguration {
    var reportOnly: Boolean = false
    var includeResponseOrigin: Boolean = true
    var allowedUrlPatterns: Set<String> = emptySet()
    var reportTo: URL? = null
}