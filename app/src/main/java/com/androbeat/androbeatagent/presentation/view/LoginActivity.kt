package com.androbeat.androbeatagent.presentation.view

import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.LoginResult
import com.androbeat.androbeatagent.databinding.ActivityLoginBinding
import com.androbeat.androbeatagent.presentation.WindowUtils.setTransparentStatusBarAndNavigationBar
import com.androbeat.androbeatagent.presentation.notifications.showToast
import com.androbeat.androbeatagent.presentation.viewmodel.LoginViewModel
import com.androbeat.androbeatagent.presentation.viewmodel.LoginViewModelFactory
import com.androbeat.androbeatagent.utils.extensions.afterTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory



    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setTransparentStatusBarAndNavigationBar(window)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel = ViewModelProvider(this, loginViewModelFactory)[LoginViewModel::class.java]

        checkTokenStored()
        observeViewModel()

        binding.username.afterTextChanged {
            loginViewModel.loginDataChanged(it)
        }

        binding.login.setOnClickListener {
            handleLoginClick()
        }

        val reinstallText: TextView = findViewById(R.id.reinstallAgentText)
        reinstallText.setOnClickListener {
            val intent = Intent(this, ReinstallActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkTokenStored() {
        lifecycleScope.launch {
            if (loginViewModel.isTokenExists()) {
                if(loginViewModel.checkTokenStatus()) {
                    navigateToMainActivity()
                    finish()
                    return@launch
                }
            }
        }
    }

    private fun handleLoginClick() {
        binding.loading.visibility = View.VISIBLE
        lifecycleScope.launch {
            val model = Build.MODEL
            val manufacturer = Build.MANUFACTURER

            // Retrieve the main Google account
            val accountManager = AccountManager.get(this@LoginActivity)
            val accounts = accountManager.getAccountsByType("com.google")
            val mainAccountName = if (accounts.isNotEmpty()) accounts[0].name else "No Google account"

            // Log the manufacturer and main Google account
            Logger.logDebug("LoginActivity", "Manufacturer: $manufacturer")
            Logger.logDebug("LoginActivity", "Main Google Account: $mainAccountName")
            Logger.logDebug("LoginActivity", "Model: $model")

            loginViewModel.enroll(this@LoginActivity, binding.username.text.toString(),
                model, manufacturer, mainAccountName)
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?: return@Observer

            binding.login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                binding.username.error = getString(loginState.usernameError)
            }
        })

        loginViewModel.loginResult.observe(this, Observer {
            val loginResult = it ?: return@Observer

            binding.loading.visibility = View.GONE

            when (loginResult) {
                is LoginResult.Success -> {
                    if (loginResult.data) {
                        saveToken(binding.username.text.toString())
                        updateUi()
                        setResult(Activity.RESULT_OK)
                    } else {
                        showLoginFailed(R.string.login_failed)
                    }
                }
                is LoginResult.Error -> showLoginFailed(R.string.login_failed)
            }
            setResult(Activity.RESULT_CANCELED)
        })

    }

    private fun saveToken(token: String) {
        loginViewModel.saveToken(token)
    }

    private fun updateUi() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        showToast(errorString)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}