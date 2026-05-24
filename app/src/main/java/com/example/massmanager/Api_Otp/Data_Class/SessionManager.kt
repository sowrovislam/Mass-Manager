package com.example.massmanager.Api_Otp.Data_Class



import android.content.Context
class SessionManager(context: Context) {

    private val pref =
        context.getSharedPreferences("USER_SESSION", Context.MODE_PRIVATE)

    // ✅ Save Login
    fun saveLogin(userId: Int?, name: String) {
        val editor = pref.edit()

        if (userId != null) {
            editor.putInt("user_id", userId)
            editor.putBoolean("isLoggedIn", true)
        }

        editor.putString("user_name", name)
        editor.apply()
    }






    fun save(admin_Id: Int?, name: String) {
        val editor = pref.edit()

        if (admin_Id != null) {
            editor.putInt("admin_Id", admin_Id)
            editor.putBoolean("isLoggedIn", true)
        }

        editor.putString("username", name)
        editor.apply()
    }



    fun adminId(): Int {
        return pref.getInt("admin_Id", -1)
    }



    // ✅ Check Login
    fun isLoggedIn(): Boolean {
        return pref.getBoolean("isLoggedIn", false)
    }

    // ✅ Get User ID
    fun getUserId(): Int {
        return pref.getInt("user_id", -1)
    }

    // ✅ Get User Name (you missed this)
    fun getUserName(): String? {
        return pref.getString("user_name", null)
    }

    // ✅ Logout
    fun logout() {
        pref.edit().clear().apply()
    }
}