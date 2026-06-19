package com.example.massmanager.Api_Otp.Data_Class



import android.content.Context
class SessionManager(context: Context) {

    private val pref =
        context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    fun saveLogin(userId: Int?, email: String, name: String, role: String) {
        val editor = pref.edit()

        if (userId != null) {
            editor.putInt("user_id", userId)
        }

        editor.putString("user_name", name)
        editor.putString("role", role)   // 🔥 IMPORTANT
        editor.putBoolean("isLoggedIn", true)
        editor.putString("email_id", email)


        editor.apply()
    }

    fun getUserId(): Int {
        return pref.getInt("user_id", -1)
    }

    fun getUserName(): String? {
        return pref.getString("user_name", null)
    }
    fun GetEmail(): String? {
        return pref.getString("email_id", null)
    }

    fun getRole(): String {
        return pref.getString("role", "user") ?: "user"
    }

    fun isAdmin(): Boolean {
        return getRole() == "admin"
    }
    fun isLoggedIn(): Boolean { return pref.getBoolean("isLoggedIn", false) }
    fun logout() {
        pref.edit().clear().apply()
    }
}