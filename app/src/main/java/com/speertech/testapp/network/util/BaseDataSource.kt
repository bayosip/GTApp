package com.speertech.testapp.network.util

import com.silverorange.videoplayer.network.util.Resource
import retrofit2.Response
import java.lang.Exception

/**
 * Handles the conversion of http response to Resource
 */
abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend ()->Response<T?>):Resource<T>{
        try {
            val response = call()
            if (response.isSuccessful){
                val body = response.body()
                if (body !=null) return Resource.success(body)
            }
            return  Resource.error(response.message())
        }catch (ex :Exception){
            return Resource.error(ex.message?:ex.toString())
        }
    }
}