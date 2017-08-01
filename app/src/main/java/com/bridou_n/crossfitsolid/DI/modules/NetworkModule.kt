package com.bridou_n.crossfitsolid.DI.modules

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.API.LoginService
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.models.classes.LoginRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getIso8601Format
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by bridou_n on 25/07/2017.
 */

@Module class NetworkModule {

    val BOOKING_BASE_URL = "https://ssl1.brpsystems.se/crossfitsolidgog/api/ver3/"
    val WOD_BASE_URL = "http://www.crossfitsolid.se/"
    val AUTHORIZATION = "Authorization"

    @Provides @Singleton @Named("injectToken")
    fun provideInjectTokenInterceptor(prefs: PreferencesManager) : Interceptor {
        return Interceptor { chain ->
            val req = chain.request()

            val newReq = req.newBuilder()
                    .header(AUTHORIZATION, "Bearer ${prefs.getToken()}")
                    .method(req.method(), req.body())
                    .build()

            chain.proceed(newReq)
        }
    }

    @Provides @Singleton @Named("logging")
    fun provideLoggingInterceptor() : Interceptor {
        return Interceptor { chain ->
            val req = chain.request()

            // Logging what's happenning
            val t1 = System.nanoTime()
            Log.d("OKHTTP3", "${req.method()} ==> ${req.url()} on ${chain.connection()}\nHeaders: ${req.headers()}Body: ${req.body().toString()}")

            val response = chain.proceed(req)

            val bodyString = response.body()?.string()
            val executionTime = (System.nanoTime() - t1) / 1e6
            Log.d("OKHTTP3", "${response.request().method()} <== (${response.code()}) ${response.request().url()} in $executionTime ms \n$bodyString")

            response.newBuilder()
                    .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                    .build()
        }
    }

    @Provides @Singleton
    fun provideAuthenticator(prefs: PreferencesManager, loginService: LoginService) : Authenticator {
        fun reAuthIfNeeded(response: Response) : Request? {
            Log.d("Authenticator", "Authenticating for response: " + response)
            Log.d("Authenticator", "Trying with username=${prefs.getUsername()} - password=${prefs.getPassword()}")

            if (prefs.getUsername() == null || prefs.getPassword() == null) {
                return null
            }

            val resp = loginService.login(LoginRequest(prefs.getUsername() ?: "", prefs.getPassword() ?: "")).blockingGet()

            prefs.setToken(resp.access_token)
            prefs.setUserId(resp.username)

            return response.request().newBuilder()
                    .header(AUTHORIZATION, "Bearer ${prefs.getToken()}")
                    .build()
        }

        return Authenticator { _, response -> reAuthIfNeeded(response) }
    }


    @Provides @Singleton @Named("login")
    fun provideLoginOkHttpClient(@Named("logging") loggingInterceptor: Interceptor) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides @Singleton
    fun provideOkHttpClient(@Named("injectToken") injectTokenInterceptor: Interceptor,
                            @Named("logging") loggingInterceptor: Interceptor,
                            authenticator: Authenticator) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(injectTokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .authenticator(authenticator)
                .build()
    }

    @Provides @Singleton
    fun provideGson() = GsonBuilder().setDateFormat(getIso8601Format()).create()

    @Provides @Singleton @Named("login")
    fun provideLoginRetrofit(@Named("login") httpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(BOOKING_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
    }

    @Provides @Singleton @Named("wods")
    fun provideWodsRetrofit(httpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(WOD_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(httpClient)
                .build()
    }

    @Provides @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(BOOKING_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
    }

    @Provides @Singleton
    fun provideLoginService(@Named("login") retrofit: Retrofit) = retrofit.create(LoginService::class.java)

    @Provides @Singleton
    fun provideBookingService(retrofit: Retrofit) = retrofit.create(BookingService::class.java)

    @Provides @Singleton
    fun provideWodsService(@Named("wods") retrofit: Retrofit) = retrofit.create(WodsService::class.java)
}