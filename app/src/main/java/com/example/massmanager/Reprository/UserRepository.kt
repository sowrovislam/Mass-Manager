package com.example.massmanager.Reprository

import com.example.massmanager.Api_Otp.Data_Class.ApiService

class UserRepository(private val api: ApiService) {

    suspend fun getUsers(usersid: String) =
        api.getUsers(usersid)

    suspend fun deleteUser(email: String) =
        api.deleteUser(email)
}