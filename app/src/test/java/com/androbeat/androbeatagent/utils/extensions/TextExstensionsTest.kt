// TextExtensionsTest.kt
package com.androbeat.androbeatagent.utils.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TextExtensionsTest {

    private lateinit var editText: EditText

    @Before
    fun setUp() {
        editText = mockk(relaxed = true)
    }

    @Test
    fun `afterTextChanged should invoke callback after text changed`() {
        val callback = mockk<(String) -> Unit>(relaxed = true)
        val textWatcherSlot = slot<TextWatcher>()

        every { editText.addTextChangedListener(capture(textWatcherSlot)) } just Runs

        editText.afterTextChanged(callback)

        val textWatcher = textWatcherSlot.captured
        val editable = mockk<Editable>(relaxed = true)
        every { editable.toString() } returns "test"

        textWatcher.afterTextChanged(editable)

        verify { callback.invoke("test") }
    }
}