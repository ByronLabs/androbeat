package com.androbeat.androbeatagent.presentation.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.androbeat.androbeatagent.data.daos.TokenDao
import com.androbeat.androbeatagent.data.model.models.LoginResult
import com.androbeat.androbeatagent.data.model.models.TokenModel
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.data.repository.LoginRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var loginRepository: LoginRepository

    @MockK
    private lateinit var appDatabase: AppDatabase

    private lateinit var loginViewModel: LoginViewModel

    @MockK
    private lateinit var loginResultObserver: Observer<LoginResult>

    @MockK
    private lateinit var loginFormStateObserver: Observer<LoginFormState>

    @MockK
    private lateinit var context: Context

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(loginRepository, appDatabase)
        loginViewModel.loginResult.observeForever(loginResultObserver)
        loginViewModel.loginFormState.observeForever(loginFormStateObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun saveToken() = runTest {
        // Arrange
        val token = "test_token"
        val tokenDao = mockk<TokenDao>(relaxed = true)
        every { appDatabase.tokenDao() } returns tokenDao

        // Act
        loginViewModel.saveToken(token)

        // Assert
        coVerify { tokenDao.insert(TokenModel(token = token)) }
    }

    @Test
    fun isTokenExists() = runTest {
        // Arrange
        val tokenDao = mockk<TokenDao>(relaxed = true)
        every { appDatabase.tokenDao() } returns tokenDao
        coEvery { tokenDao.getToken() } returns TokenModel(token = "test_token")

        // Act
        val result = loginViewModel.isTokenExists()

        // Assert
        assertTrue(result)
        coVerify { tokenDao.getToken() }
    }

    @Test
    fun loginDataChangedValid() {
        // Arrange
        val token = "valid_token"
        val loginFormStateSlot = slot<LoginFormState>()
        every { loginFormStateObserver.onChanged(capture(loginFormStateSlot)) } answers { }

        // Act
        loginViewModel.loginDataChanged(token)

        // Assert
        verify { loginFormStateObserver.onChanged(capture(loginFormStateSlot)) }
        assertTrue(loginFormStateSlot.captured.isDataValid)
    }

    @Test
    fun loginDataChangedInvalid() {
        // Arrange
        val token = "123"
        val loginFormStateSlot = slot<LoginFormState>()
        every { loginFormStateObserver.onChanged(capture(loginFormStateSlot)) } answers { }

        // Act
        loginViewModel.loginDataChanged(token)

        // Assert
        verify { loginFormStateObserver.onChanged(capture(loginFormStateSlot)) }
        assertFalse(loginFormStateSlot.captured.isDataValid)
    }
}