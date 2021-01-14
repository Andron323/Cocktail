package cocktail.info

import cocktail.info.api.ApiCocktail
import cocktail.info.api.ApiFilter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/"

    val INSTANCE_COCKTAIL: ApiCocktail by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiCocktail::class.java)
    }

    val INSTANCE_FILTER: ApiFilter by lazy{
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiFilter::class.java)
    }
}