package com.poetry.on.quotescreaterwakeel.model

import com.poetry.on.quotescreaterwakeel.R


data class LanguageModel(
    var country_name: String,
    var country_code: String,
    var country_flag: Int = R.drawable.ic_launcher_foreground,
    var check: Boolean,
)