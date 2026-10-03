package no.jksolbakken

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

class ConnectionAllowlistPluginTest {

    @Test
    fun `response origin token should be included by default`() = testApplication {
        application {
            testModule()
        }
        val response = client.get("/")
        assertEquals("(response-origin)", response.headers["Connection-Allowlist"])
    }

    @Test
    fun `other origins should be quoted and space separated`() = testApplication {
        application {
            testModule(urlPatterns = setOf("http://whatever", "http://something"))
        }
        val response = client.get("/")
        assertEquals("""(response-origin "http://whatever" "http://something")""", response.headers["Connection-Allowlist"])
    }

    @Test
    fun `report-to token and URL should be set if provided`() = testApplication {
        application {
            testModule(dryRun = false,
                includeThisOrigin = true,
                urlPatterns = setOf("http://whatever", "http://something"),
                reportingEndpoint = "http://reporting.example.com")
        }
        val response = client.get("/")
        assertEquals("""(response-origin "http://whatever" "http://something"); report-to=default""", response.headers["Connection-Allowlist"])
        assertEquals("""default="http://reporting.example.com"""", response.headers["Reporting-Endpoints"])
    }

    @Test
    fun `Connection-Allowlist header should have a different name if in report only mode`() = testApplication {
        application {
            testModule(dryRun = true, reportingEndpoint = "http://reporting.example.com")
        }
        val response = client.get("/")
        assertEquals("""(response-origin); report-to=default""", response.headers["Connection-Allowlist-Report-Only"])
        assertNull(response.headers["Connection-Allowlist"])
        assertEquals("""default="http://reporting.example.com"""", response.headers["Reporting-Endpoints"])
    }

}

fun Application.testModule(
    dryRun: Boolean = false,
    includeThisOrigin: Boolean = true,
    urlPatterns: Set<String> = emptySet(),
    reportingEndpoint: String? = null) {

    install(ConnectionAllowlist) {
        reportOnly = dryRun
        includeResponseOrigin = includeThisOrigin
        allowedUrlPatterns = urlPatterns
        reportTo = reportingEndpoint
    }

    routing {
        get("/") {
            call.respond(HttpStatusCode.OK)
        }
    }
}