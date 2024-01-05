package org.hyperskill.projectname.internals

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.junit.Assert.*
import org.robolectric.Robolectric
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.shadow.api.Shadow
import org.robolectric.shadows.ShadowActivity
import org.robolectric.shadows.ShadowAlertDialog
import org.robolectric.shadows.ShadowLooper
import org.robolectric.shadows.ShadowToast
import java.time.Duration

@Suppress("RedundantUnitReturnType")
abstract class AbstractUnitTest<T : Activity>(clazz: Class<T>) {

    /**
     * Setup and control activities and their lifecycle
     */
    val activityController: ActivityController<T> by lazy {
        Robolectric.buildActivity(clazz)
    }

    /**
     * The activity being tested.
     *
     * It is the @RealObject of the shadowActivity
     */
    val activity : Activity by lazy {
        activityController.get()
    }

    /**
     * A Roboletric shadow object of the Activity class, contains helper methods to deal with
     * testing activities like setting permissions, peeking results of launched activities for result,
     * retrieving shown dialogs, intents and others.
     *
     * If you don't know what shadows are you can have a better understanding on that reading this
     * on roboletric documentation: http://robolectric.org/extending/
     *
     * Understanding Shadows is fundamental for Roboletric, things are not what they appear to be on
     * Roboletric because running a code on the jvm is not the same as running the code on a real/emulated device.
     * Code that expects to eventually talk to the machine won't have the machine they expect to have to talk to.
     * Shadow is how Roboletric makes things possible, they impersonate @RealObject and act when @RealObject is expected to act.
     *
     * Things in Roboletric are not what they appear to be.
     * It is possible to not notice it for the most part, but it will be essential for some other parts
     */
    val shadowActivity: ShadowActivity by lazy {
        Shadow.extract(activity)
    }

    /**
     * A Roboletric shadow object of the mainLooper. Handles enqueued runnables and also the passage of time.
     *
     * Usually used with .idleFor(someDurationValue) or .runToEndOfTasks()
     */
    val shadowLooper: ShadowLooper by lazy {
        shadowOf(activity.mainLooper)
    }

    /**
     * Sets up activity for tests and ensures better error messages displayed
     * when tests are run with check button and exceptions are thrown by user implementation.
     *
     * returns a value for convenience use, like in tests that involve navigation between Activities
     */
    fun <ReturnValue> testActivity(
        arguments: Intent = Intent(),
        savedInstanceState: Bundle? = null,
        testCodeBlock: (Activity) -> ReturnValue
    ): ReturnValue {
        try {
            activity.intent = arguments
            if (savedInstanceState == null) {
                activityController.setup()
            } else {
                activityController.setup(savedInstanceState)
            }
        } catch (ex: Exception) {
            throw AssertionError("Exception, test failed on activity creation with $ex\n${ex.stackTraceToString()}")
        }

        return try {
            testCodeBlock(activity)
        } catch (ex: Exception) {
            throw AssertionError("Exception. Test failed on activity execution with $ex\n${ex.stackTraceToString()}")
        }
    }

    /**
     * Use this method to find views.
     *
     * The view existence will be assert before being returned
     */
    inline fun <reified T> Activity.findViewByString(idString: String): T {
        val id = this.resources.getIdentifier(idString, "id", this.packageName)
        val maybeView: View? = this.findViewById(id)

        val (expectedClass, maybeActualClass) =
            if(T::class.java.simpleName == maybeView?.javaClass?.simpleName) {
                T::class.java.canonicalName to maybeView?.javaClass?.canonicalName
            } else {
                T::class.java.simpleName to maybeView?.javaClass?.simpleName
            }
        val idNotFoundMessage = "View with id \"$idString\" was not found"
        val wrongClassMessage = "View with id \"$idString\" is not from expected class. " +
                "Expected $expectedClass found $maybeActualClass"

        assertNotNull(idNotFoundMessage, maybeView)
        assertTrue(wrongClassMessage, maybeView is T)

        return maybeView as T
    }

    /**
     * Use this method to find views.
     *
     * The view existence will be assert before being returned
     */
    inline fun <reified T> View.findViewByString(idString: String): T {
        val id = this.resources.getIdentifier(idString, "id", context.packageName)
        val maybeView: View? = this.findViewById(id)

        val (expectedClass, maybeActualClass) =
            if(T::class.java.simpleName == maybeView?.javaClass?.simpleName) {
                T::class.java.canonicalName to maybeView?.javaClass?.canonicalName
            } else {
                T::class.java.simpleName to maybeView?.javaClass?.simpleName
            }
        val idNotFoundMessage = "View with id \"$idString\" was not found"
        val wrongClassMessage = "View with id \"$idString\" is not from expected class. " +
                "Expected $expectedClass found $maybeActualClass"

        assertNotNull(idNotFoundMessage, maybeView)
        assertTrue(wrongClassMessage, maybeView is T)

        return maybeView as T
    }

    /**
     * Use this method to perform clicks. It will also advance the clock millis milliseconds and run
     * enqueued Runnable scheduled to run on main looper in that timeframe.
     * Default value for millis is 500
     *
     * Internally it calls performClick() and shadowLooper.idleFor(millis)
     */
    fun View.clickAndRun(millis: Long = 500){
        this.performClick()
        shadowLooper.idleFor(Duration.ofMillis(millis))
    }

    /**
     * Asserts that the last message toasted is the expectedMessage.
     * Assertion fails if no toast is shown with null actualLastMessage value.
     */
    fun assertLastToastMessageEquals(errorMessage: String, expectedMessage: String,) {
        val actualLastMessage: String? = ShadowToast.getTextOfLatestToast()
        assertEquals(errorMessage, expectedMessage, actualLastMessage)
    }

    /**
     * Use this method to retrieve the latest AlertDialog.
     *
     * The existence of such AlertDialog will be asserted before returning.
     *
     * Robolectric only supports android.app.AlertDialog, test will not be
     * able to find androidx.appcompat.app.AlertDialog.
     *
     * - Important!!! :
     * When writing stage description state explicitly the correct version that should be imported
     */
    fun getLatestDialog(): AlertDialog {
        val latestAlertDialog = ShadowAlertDialog.getLatestAlertDialog()

        assertNotNull(
            "There was no AlertDialog found. Make sure to import android.app.AlertDialog version",
            latestAlertDialog
        )

        return latestAlertDialog!!
    }

    /**
     *  Use this to make some action on the each item of the RecyclerView.
     *
     *  Asserts that the size matches the size of fakeResultList
     *
     *  Calls action with the itemViewSupplier so that it is possible to retrieve that itemView.
     *
     *  Take attention to refresh references to views coming from itemView since RecyclerView
     *  can change the instance of View for a determinate list item after an update to the list.
     */
    fun <T> RecyclerView.doActionOnEachListItem(
        fakeResultList: List<T>,
        caseDescription: String = "",
        action: (itemViewSupplier: () -> View, position: Int, item: T) -> Unit
    ) : Unit {

        assertNotNull("$caseDescription Your recycler view adapter should not be null", this.adapter)

        val expectedSize = fakeResultList.size

        val actualSize = this.adapter!!.itemCount
        assertEquals("$caseDescription Incorrect number of list items", expectedSize, actualSize)

        if(expectedSize == 0) {
            return
        } else if(expectedSize > 0) {

            val maxItemWidth = (0 until expectedSize)
                .asSequence()
                .mapNotNull { this.findViewHolderForAdapterPosition(it)?.itemView?.width }
                .maxOrNull()
                ?: throw AssertionError("$caseDescription No item is being displayed on RecyclerView, is it big enough to display one item?")
            val listWidth = maxItemWidth * (expectedSize + 1)

            val maxItemHeight = (0 until actualSize)
                .asSequence()
                .mapNotNull { this.findViewHolderForAdapterPosition(it)?.itemView?.height }
                .maxOrNull()
                ?: throw AssertionError("$caseDescription No item is being displayed on RecyclerView, is it big enough to display one item?")
            val listHeight = maxItemHeight * (actualSize + 1)

            for((i, song) in fakeResultList.withIndex()) {

                val itemViewSupplier = {
                    this.layout(0,0, listHeight, listWidth)  // may increase clock time
                    scrollToPosition(i)
                    findViewHolderForAdapterPosition(i)?.itemView
                        ?: throw AssertionError("$caseDescription Could not find list item with index $i")
                }
                action(itemViewSupplier, i, song)
            }

        } else {
            throw IllegalStateException("size assertion was not effective")
        }
    }

    /**
     *  Use this to make some action on one item of the RecyclerView.
     *
     *  Asserts that the the size of the list is at least itemIndex + 1.
     *
     *  Calls action with the itemViewSupplier so that it is possible to retrieve that itemView.
     *
     *  Take attention to refresh references to views coming from itemView since RecyclerView
     *  can change the instance of View for a determinate list item after an update to the list.
     */
    fun RecyclerView.doActionOnSingleListItem(
        itemIndex: Int,
        caseDescription: String = "",
        action: (itemViewSupplier: () -> View) -> Unit
    ) : Unit {

        assertNotNull("$caseDescription Your recycler view adapter should not be null", this.adapter)

        val expectedMinSize = itemIndex + 1

        val actualSize = this.adapter!!.itemCount
        assertTrue(
            "$caseDescription RecyclerView was expected to contain item with index $itemIndex, but its size was $actualSize",
            actualSize >= expectedMinSize
        )

        if (actualSize >= expectedMinSize) {
            val maxItemWidth = (0 until actualSize)
                .asSequence()
                .mapNotNull { this.findViewHolderForAdapterPosition(it)?.itemView?.width }
                .maxOrNull()
                ?: throw AssertionError("$caseDescription No item is being displayed on RecyclerView, is it big enough to display one item?")
            val listWidth = maxItemWidth * (actualSize + 1)

            val maxItemHeight = (0 until actualSize)
                .asSequence()
                .mapNotNull { this.findViewHolderForAdapterPosition(it)?.itemView?.height }
                .maxOrNull()
                ?: throw AssertionError("$caseDescription No item is being displayed on RecyclerView, is it big enough to display one item?")
            val listHeight = maxItemHeight * (actualSize + 1)

            val itemViewSupplier = {
                this.layout(0, 0, listWidth, listHeight)  // may increase clock time
                this.scrollToPosition(itemIndex)
                val itemView = (this.findViewHolderForAdapterPosition(itemIndex)?.itemView
                    ?: throw AssertionError("$caseDescription Could not find list item with index $itemIndex"))
                itemView
            }

            action(itemViewSupplier)

        } else {
            throw IllegalStateException("size assertion was not effective")
        }
    }
}
