package com.androbeat.androbeatagent.data.local.extractors.software


import com.androbeat.androbeatagent.data.model.models.extractors.software.EnvironmentVariableModel
import com.androbeat.androbeatagent.domain.data.DataProvider
import com.androbeat.androbeatagent.domain.extractors.DataExtractor
import com.androbeat.androbeatagent.data.enums.DataProviderType

class EnvironmentVariables : DataExtractor,
    DataProvider<List<EnvironmentVariableModel?>?> {
    private val _environmentVariables = mutableListOf<EnvironmentVariableModel>()
    private val environmentVariables: List<EnvironmentVariableModel>
        get() = _environmentVariables
    fun getEnvironmentVariablesStatistics(): String {
        _environmentVariables.clear()
        val env = System.getenv()
        for (envName in env.keys) {
            env[envName]?.let { EnvironmentVariableModel(envName, it) }
                ?.let { _environmentVariables.add(it) }
        }
        return generateLogString().toString()
    }

    private fun generateLogString(): StringBuilder {
        val environmentVariablesStringBuilder = StringBuilder("Environment Variables {")
        for (envVar in environmentVariables) {
            environmentVariablesStringBuilder.append("[")
                .append(envVar.name).append(" ")
                .append(envVar.value).append(" ")
                .append("]")
        }
        environmentVariablesStringBuilder.append("}")
        return environmentVariablesStringBuilder
    }

    override val statistics: String
        get() = getEnvironmentVariablesStatistics()
    override val dataProvider: DataProvider<*>
        get() = this
    override val type: DataProviderType
        get() = DataProviderType.ENV_VARIABLE_LIST
    override val data: List<EnvironmentVariableModel?>
        get() = environmentVariables

    companion object {
        private val TAG = EnvironmentVariables::class.java.simpleName
    }

}