import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.androbeat.androbeatagent.data.local.extractors.software.BasicConfiguration
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BasicConfigurationTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val basicConfig = BasicConfiguration(context)

    @Test
    fun checkIsEmulatorShouldReturnTrueOnEmulator() {
        val method = basicConfig.javaClass.getDeclaredMethod("checkIsEmulator").apply { isAccessible = true }
        val result = method.invoke(basicConfig) as Boolean

        if (isEmulatorDevice()) {
            assertTrue(result)
        }
    }

    private fun isEmulatorDevice(): Boolean {
        return android.os.Build.FINGERPRINT.startsWith("generic") ||
                android.os.Build.MODEL.contains("Emulator") ||
                android.os.Build.MANUFACTURER.contains("Genymotion") ||
                android.os.Build.PRODUCT == "sdk_google_phone_x86"
    }
}