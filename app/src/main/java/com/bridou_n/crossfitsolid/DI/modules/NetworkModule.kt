package com.bridou_n.crossfitsolid.DI.modules

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.models.LoginRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by bridou_n on 25/07/2017.
 */

@Module class NetworkModule {

    @Provides @Singleton @Named("injectToken")
    fun provideInjectTokenInterceptor() : Interceptor {
        return Interceptor { chain ->
            val req = chain.request()

            val newReq = req.newBuilder()
                    .header("Authorization", "blabla")
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
            Log.i("OKHTTP3", String.format("Sending request ${req.url()} on %s%n%s%s", chain.connection(), req.headers(), req.body()))

            val response = chain.proceed(req)

            val t2 = System.nanoTime()
            val bodyString = response.body()?.string()
            Log.i("OKHTTP3", String.format(Locale.getDefault(),
                    "Received response for %s in %.1fms%n(%s)%s", response.request().url(), (t2 - t1) / 1e6, response.code(), bodyString))

            response.newBuilder()
                    .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                    .build()
        }
    }

    @Provides @Singleton
    fun provideAuthenticator(prefs: PreferencesManager, gson: Gson) : Authenticator {
        fun reAuthIfNeeded(route : Route, response: Response) : Request? {
            Log.d("Authenticator", "Authenticating for response: " + response)
            Log.d("Authenticator", "Challenges: " + response.challenges())
            Log.d("Authenticator", "Trying with ${prefs.getUsername()} - ${prefs.getPassword()}")

            if (prefs.getUsername() == null || prefs.getPassword() == null) {
                return null
            }

            val postPayload = gson.toJson(LoginRequest(prefs.getUsername() ?: "", prefs.getPassword() ?: ""))

            return response.request().newBuilder()
                    .post(RequestBody.create(null, postPayload))
                    .build()
        }

        return Authenticator { route, response -> reAuthIfNeeded(route, response) }
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
    fun provideGson() = GsonBuilder().create()

    @Provides @Singleton
    fun provideRetrofit(httpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://ssl1.brpsystems.se/crossfitsolidgog/api/ver3/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
    }

    @Provides @Singleton
    fun provideBookingService(retrofit: Retrofit) = retrofit.create(BookingService::class.java)
}