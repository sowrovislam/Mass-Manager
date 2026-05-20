package com.example.massmanager.Api_Otp.Data_Class



import android.content.Context
class SessionManager(context: Context) {

    private val pref =
        context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    // Save Login
    fun saveLogin() {

        pref.edit()
            .putBoolean("isLoggedIn", true)
            .apply()
    }

    // Check Login
    fun isLoggedIn(): Boolean {

        return pref.getBoolean("isLoggedIn", false)
    }

    // Logout
    fun logout() {

        pref.edit()
            .clear()
            .apply()
    }
}