package com.androbeat.androbeatagent.presentation.tabFragments

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.androbeat.androbeatagent.R
import com.androbeat.androbeatagent.data.local.BeatService
import com.androbeat.androbeatagent.data.logger.Logger
import com.androbeat.androbeatagent.data.model.models.DeviceId
import com.androbeat.androbeatagent.data.repository.AppDatabase
import com.androbeat.androbeatagent.databinding.FragmentStatusBinding
import com.androbeat.androbeatagent.domain.permissions.PermissionsManager
import com.androbeat.androbeatagent.domain.usecase.StartSensorUseCase
import com.androbeat.androbeatagent.presentation.viewmodel.LoginViewModelFactory
import com.androbeat.androbeatagent.presentation.viewmodel.MainViewModel
import com.androbeat.androbeatagent.utils.ApplicationStatus
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class StatusFragment : Fragment(R.layout.fragment_status) {

    private lateinit var binding: FragmentStatusBinding
    private var mAdminName: ComponentName? = null
    private var mDPM: DevicePolicyManager? = null
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var permissionsManager: PermissionsManager

    @Inject
    lateinit var appDatabase: AppDatabase

    @Inject
    lateinit var loginViewModelFactory: LoginViewModelFactory

    @Inject
    lateinit var startSensorUseCase: StartSensorUseCase

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 123
        const val LOG_TAG = "MainActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        binding = FragmentStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkAndUpdateSavedDeviceId()
        initializePermissionsManagerImp()
        fetchAndSetClientId()

        startServiceIfNotRunning()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getClientIdFromRoom()
        }

        val deviceAdminLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Logger.logDebug(LOG_TAG, "Device Admin: enabled")
                } else {
                    Logger.logDebug(LOG_TAG, "Device Admin: not enabled")
                }
            }

        permissionsManager.requestDeviceAdminRights(requireContext(), deviceAdminLauncher)
        permissionsManager.requestDoNotDisturbPermission(requireContext(), requireActivity())
    }

    private fun fetchAndSetClientId() {
        viewLifecycleOwner.lifecycleScope.launch {
            val clientId = appDatabase.deviceIdDao().getDeviceId()?.deviceId ?: "Unknown client ID"
            binding.clientIdText.text = clientId
        }
    }

    private fun checkAndUpdateSavedDeviceId() {
        viewLifecycleOwner.lifecycleScope.launch {
            val storedDeviceId = appDatabase.deviceIdDao().getDeviceId()?.deviceId
            val actualDeviceId = getActualDeviceId()

            if (storedDeviceId != null && storedDeviceId != actualDeviceId) {
                updateDeviceId(storedDeviceId, actualDeviceId)
            }
        }
    }

    private suspend fun getActualDeviceId(): String {
        val adInfo = withContext(Dispatchers.IO) {
            AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
        }
        return adInfo.id ?: "unknown_device_id"
    }

    private suspend fun updateDeviceId(storedDeviceId: String, actualDeviceId: String) {
        try {
            Logger.logDebug(LOG_TAG, "Device ID changed locally from $storedDeviceId to $actualDeviceId")
            appDatabase.deviceIdDao().updateDeviceId(DeviceId(1, actualDeviceId))
            ApplicationStatus.postOK()
        } catch (e: Exception) {
            ApplicationStatus.postError("Error modifying Device ID: ${e.message}")
            Logger.logError(LOG_TAG, "Error modifying Device ID $e")
        }
    }

    private fun initializePermissionsManagerImp() {
        permissionsManager.requestAppPermissions(PERMISSIONS_REQUEST_CODE)
    }

    private fun startServiceIfNotRunning() {
        val serviceIntent = Intent(requireContext(), BeatService::class.java)
        val isRunning = detectIfBeatServiceIsRunning()

        if (!isRunning) {
            try {
                startSensorUseCase.startBeatService(serviceIntent)
                binding.stateTextView.text = getString(R.string.service_running)
            } catch (e: Exception) {
                Logger.logError(LOG_TAG, "Error starting service: $e")
                Toast.makeText(
                    requireContext(),
                    "Error starting service:\n${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                binding.stateTextView.text = getString(R.string.service_not_running)
            }
        } else {
            binding.stateTextView.text = getString(R.string.service_running)
        }
    }

    private fun detectIfBeatServiceIsRunning(): Boolean {
        val manager = requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BeatService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
