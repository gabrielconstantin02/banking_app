package com.example.banking_app

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.example.banking_app.models.User

class MApplication : Application() {
    companion object {
        @kotlin.jvm.JvmField
        var currentUser: User? = null

        fun logout(activity: Activity) {
            currentUser = null
            Intent(activity, Login::class.java).also {
                activity.startActivity(it)
            }
        }
    }
}