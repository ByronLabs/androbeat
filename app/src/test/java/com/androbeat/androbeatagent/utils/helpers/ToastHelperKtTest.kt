package com.androbeat.androbeatagent.utils.helpers

import android.content.Context
import android.widget.Toast
import com.androbeat.androbeatagent.presentation.notifications.showToast
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class ToastHelperTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mockk(relaxed = true)
        mockkStatic(Toast::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(Toast::class)
    }

    @Test
    fun showToast_shouldShowToastWithCorrectMessage() {
        val messageResId = android.R.string.ok
        val toast: Toast = mockk(relaxed = true)

        every { Toast.makeText(context, messageResId, Toast.LENGTH_SHORT) } returns toast

        context.showToast(messageResId)

        verify { Toast.makeText(context, messageResId, Toast.LENGTH_SHORT) }
        verify { toast.show() }
    }
}