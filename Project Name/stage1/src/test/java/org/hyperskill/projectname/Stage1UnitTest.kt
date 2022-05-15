package org.hyperskill.projectname

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import org.hyperskill.projectname.internals.AbstractUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class Stage1UnitTest : AbstractUnitTest<MainActivity>(MainActivity::class.java){

    private val helloTv: TextView by lazy {
        //activity.findViewById<TextView>(R.id.someInexistingId) // don't use findViewById, if id does not exist the code won't compile. We want to make as few assumptions as possible from the actual implementation, that is, the implementation is a "black box" for us
        val view = activity.findViewByString<TextView>("helloTv")

        // initialization assertions - if a view should always begin with some specific state better assert it here on initialization since @Test methods may be called in any order. This ensures the view will have the correct initial state for all @Test methods
        val messageInitialText = "The helloTv has a wrong initial text"
        val expectedInitialText = "hello new project"
        val actualInitialText = view.text.toString().lowercase()
        assertEquals(messageInitialText, expectedInitialText, actualInitialText)

        view
    }

    private val submitBtn: Button by lazy {
        val view = activity.findViewByString<Button>("submitBtn")

        val messageInitialText = "The submitBtn has the wrong text"
        val expectedInitialText = "submit"
        val actualInitialText = view.text.toString().lowercase()
        assertEquals(messageInitialText, expectedInitialText, actualInitialText)

        view
    }

    private val unexistingView: View by lazy {
        activity.findViewByString("unexistingView")
    }

    private val wrongClassView: ImageButton by lazy {
        activity.findViewByString("submitBtn")
    }

    private val inputEt: EditText by lazy {
        activity.findViewByString("inputEt")
    }


    @Test
    fun checkHelloTvInitialState() {
        // helloTv // before testActivity block the view won't be found, because activity won't be setup

        testActivity {
            helloTv  // lazy initialization will happen here
        }
    }

    @Test
    fun checkSubmitButtonInitialState() {
        testActivity {
            submitBtn
        }
    }

    @Test
    fun checkUnexistingViewInitialState() {
        testActivity {
            unexistingView  // if user does not declare an expected view test will fail because of findViewByString implementation
        }
    }

    @Test
    fun checkWrongClassViewInitialState() {
        testActivity {
            wrongClassView // if  user does not declare the view with the expected class/subclass test will fail because of findViewByString implementation
        }
    }

    @Test
    fun checkSomeBusinessLogic() {
        testActivity {
            // initialize all lazy fields used on test - to ensure field initialization is made with initial state of activity
            submitBtn
            helloTv
            inputEt
            //

            // set up tested state
            inputEt.setText("some text")
            submitBtn.clickAndRun()  // preferred method for making clicks, it will advance the clock and run enqueued runnable in mainLooper on that timeframe, default 500milli accepts long as argument

            // make assertions on given state
            val messageExpectedChangeHelloText = "If the inputEt is not empty and the submitBtn is clicked helloTv should change text"
            val expectedText = "some text"
            val actualText = helloTv.text.toString()
            assertEquals(messageExpectedChangeHelloText, expectedText, actualText)

            // set up following state (consider using new @Test methods if possible and not expensive)
            // make more assertions
            // ...
        }
    }
}