package com.moose.foodies.remote

import com.moose.foodies.features.auth.Auth
import com.moose.foodies.features.auth.Credentials
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiEndpoints {

    @POST("/api/users/login")
    suspend fun login(@Body credentials: Credentials): Auth

    @POST("/api/auth/signup")
    suspend fun signup(@Body credentials: Credentials): Auth

    @GET("/api/auth/forgot/{email}")
    suspend fun forgot(@Path("email") email: String): Auth
}