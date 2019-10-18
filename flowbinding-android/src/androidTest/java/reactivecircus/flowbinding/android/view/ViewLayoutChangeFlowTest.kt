package reactivecircus.flowbinding.android.view

import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import org.amshove.kluent.shouldEqual
import org.junit.Test
import reactivecircus.flowbinding.android.fixtures.view.AndroidViewFragment
import reactivecircus.flowbinding.testing.FlowRecorder
import reactivecircus.flowbinding.testing.launchTest
import reactivecircus.flowbinding.testing.recordWith

@LargeTest
class ViewLayoutChangeFlowTest {

    private val appContext = ApplicationProvider.getApplicationContext<Context>().applicationContext

    @Test
    fun viewLayoutChanges() {
        launchTest<AndroidViewFragment> {
            val recorder = FlowRecorder<Unit>(testScope)
            val view = View(appContext)

            view.layoutChanges().recordWith(recorder)

            recorder.assertNoMoreValues()

            runOnUiThread {
                view.layout(view.left - 5, view.top - 5, view.right, view.bottom)
            }
            recorder.takeValue() shouldEqual Unit
            recorder.assertNoMoreValues()

            cancelTestScope()

            runOnUiThread {
                view.layout(view.left, view.top, view.right, view.bottom)
            }
            recorder.assertNoMoreValues()
        }
    }
}
