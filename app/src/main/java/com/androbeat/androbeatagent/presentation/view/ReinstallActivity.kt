package com.androbeat.androbeatagent.presentation.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.BuildConfig
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.databinding.ActivityReinstallBinding
import com.androbeat.androbeatagent.presentation.WindowUtils.setTransparentStatusBarAndNavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ReinstallActivity : AppCompatActivity() {

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

        binding.ReinstallButton.setOnClickListener {
            val token = binding.OriginalTokenTextInput.text.toString()
            if (token.trim() != BuildConfig.ENROLLMENT_TOKEN.trim()) {
                showErrorDialog()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        appDatabase.tokenDao().clear()
                        appDatabase.clientIdDao().clear()
                        appDatabase.deviceIdDao().clear()
                    }
                    startActivity(Intent(this@ReinstallActivity, LoginActivity::class.java))
                    finish()
                } catch (e: Exception) {
                    Logger.logError("ReinstallActivity", "Error: ${e.message}")
                    showErrorDialog()
                }
            }
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
