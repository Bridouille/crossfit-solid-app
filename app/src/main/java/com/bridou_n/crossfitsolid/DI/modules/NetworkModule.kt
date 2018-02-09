package com.bridou_n.crossfitsolid.DI.modules

import android.util.Log
import com.bridou_n.crossfitsolid.API.BookingService
import com.bridou_n.crossfitsolid.API.LoginService
import com.bridou_n.crossfitsolid.API.WodsService
import com.bridou_n.crossfitsolid.models.classes.LoginRequest
import com.bridou_n.crossfitsolid.utils.PreferencesManager
import com.bridou_n.crossfitsolid.utils.copyPaste.GsonUTCDateAdapter
import com.bridou_n.crossfitsolid.utils.extensionFunctions.getIso8601Format
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.*
import javax.inject.Named
import javax.inject.Singleton
import android.R.id.edit



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
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides @Singleton @Named("insertCookies")
    fun provideInsertCookiesInterceptor(prefs: PreferencesManager) : Interceptor {
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            val cookies = prefs.getCookies()

            cookies?.let {
                for (cookie in cookies) {
                    builder.addHeader("Cookie", cookie)
                    // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
                    Log.d("OkHttp", "Adding Header: " + cookie)
                }
            }

            chain.proceed(builder.build())
        }
    }

    @Provides @Singleton @Named("storeCookies")
    fun provideStoreCookiesInterceptor(prefs: PreferencesManager) : Interceptor {
        return Interceptor {chain ->
            val originalResponse = chain.proceed(chain.request())

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                val cookies = HashSet<String>()

                for (header in originalResponse.headers("Set-Cookie")) {
                    cookies.add(header)
                    Log.d("OkHttp", "Storing cookie: $header")
                }

                prefs.setCookies(cookies)
            }

            originalResponse
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
                            @Named("storeCookies") storeCookiesInterceptor: Interceptor,
                            @Named("insertCookies") insertCookiesInterceptor: Interceptor,
                            authenticator: Authenticator) : OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(injectTokenInterceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(storeCookiesInterceptor)
                .addInterceptor(insertCookiesInterceptor)
                .authenticator(authenticator)
                .build()
    }

    @Provides @Singleton // setDateFormat(getIso8601Format())
    fun provideGson() = GsonBuilder().registerTypeAdapter(Date::class.java, GsonUTCDateAdapter()).create()

    @Provides @Singleton @Named("login")
    fun provideLoginRetrofit(@Named("login") httpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(BOOKING_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build()
    }

    @Provides @Singleton @Named("wodsXml")
    fun provideWodsXmlRetrofit(httpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(WOD_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(httpClient)
                .build()
    }

    @Provides @Singleton @Named("wodsJson")
    fun provideWodsJsonRetrofit(httpClient: OkHttpClient, gson: Gson) : Retrofit {
        return Retrofit.Builder()
                .baseUrl(WOD_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
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


    @Provides @Singleton @Named("xmlFactory")
    fun provideWodsXmlService(@Named("wodsXml") retrofit: Retrofit) : WodsService {
        return retrofit.create(WodsService::class.java)
    }

    @Provides @Singleton @Named("jsonFactory")
    fun provideWodsJsonService(@Named("wodsJson") retrofit: Retrofit) : WodsService {
        return retrofit.create(WodsService::class.java)
    }
}