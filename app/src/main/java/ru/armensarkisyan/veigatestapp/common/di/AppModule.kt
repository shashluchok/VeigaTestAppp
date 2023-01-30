package ru.armensarkisyan.veigatestapp.common.di


import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.armensarkisyan.veigatestapp.common.domain.VeigaApi
import ru.armensarkisyan.veigatestapp.common.domain.interactors.retrofit.RatingsInteractor
import java.util.concurrent.TimeUnit

val appModule = module {

    single<VeigaApi> {
        Retrofit.Builder()
            .baseUrl("https://wowowcleaner.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
            )
            .build()
            .create(VeigaApi::class.java)
    }

    single { RatingsInteractor(get()) }

}



