package com.androbeat.androbeatagent.data.repository

import javax.inject.Inject

class BeatRepository @Inject constructor(
    private val beatDataSource: BeatDataSource
)