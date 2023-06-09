package com.speertech.testapp.network

import com.speertech.testapp.model.FollowModel
import com.speertech.testapp.model.SearchResults
import com.speertech.testapp.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    //path variable substitution for the api endpoint .
    // in the path we change the "user" with the string user we get from getUser Method
    // parsing the last parameter or url --> String user
    @GET("/users/{user}")
    fun getUser(@Path("user") user: String?): Response<User?>

    @GET("/users/{user}/followers")
    fun getFollowers(@Path("user")user: String?):Response<List<FollowModel>?>

    @GET("/users/{user}/following")
    fun getFollowing(@Path("user")user: String?):Response<List<FollowModel>?>

    @GET("/search/users")
    fun searchForUser(user: String?): Response<SearchResults?>
}