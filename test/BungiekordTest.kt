import org.yttr.Bungiekord
import kotlin.test.Test
import kotlin.test.assertFails

class BungiekordTest {
    @Test()
    fun testUserAgentParsing() {
        val validUserAgent = "A New Project/1.2.3 AppId/46 (+https://example.com;no@example.com)"
        val valid = Bungiekord {
            userAgent = validUserAgent
        }
        valid.config.let {
            assert(it.appName == "A New Project")
            assert(it.version == "1.2.3")
            assert(it.appId == 46)
            assert(it.webUrl == "https://example.com")
            assert(it.contactEmail == "no@example.com")
            assert(it.userAgent == validUserAgent)
        }

        assertFails {
            Bungiekord {
                userAgent = "Illegal user agent string"
            }
        }
    }
}