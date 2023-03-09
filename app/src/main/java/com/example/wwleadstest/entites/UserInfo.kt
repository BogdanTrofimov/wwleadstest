package com.example.wwleadstest.entites

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class UserInfo(
    val userId: String? = null,
    var url: String? = null,
) : Parcelable