package com.androbeat.androbeatagent.presentation.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.communication.ReinstallAgentRequest
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RESTClient
import com.androbeat.androbeatagent.data.remote.rest.restApiClient.RestApiInterface
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.databinding.ActivityReinstallBinding
import com.androbeat.androbeatagent.presentation.WindowUtils.setTransparentStatusBarAndNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ReinstallActivity : AppCompatActivity() {

    private lateinit var api: RestApiInterface
    private lateinit var binding: ActivityReinstallBinding

    @Inject
    lateinit var appDatabase: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reinstall)
        enableEdgeToEdge()
        binding = ActivityReinstallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setTransparentStatusBarAndNavigationBar(window)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        api = RESTClient.getRetrofit(this).create(RestApiInterface::class.java)

        binding.ReinstallButton.setOnClickListener {

            val token = binding.OriginalTokenTextInput.text.toString()
            val accountName = binding.MainAccountTextInput.text.toString()
            val email = binding.OriginalClientEmailTextInput.text.toString()

            val request = ReinstallAgentRequest(accountName, token, email)

            api.reinstallAgent(request).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        startActivity(Intent(this@ReinstallActivity, LoginActivity::class.java))
                    } else {
                        showErrorDialog()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Logger.logError("ReinstallActivity", "Error: ${t.message}")
                    showErrorDialog()
                }
            })
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        }.create().show()
    }

    private fun showErrorDialog() {
        showAlertDialog("Error", "Failed to reinstall agent. Please try again.")
    }
}