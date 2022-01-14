package science.involta.crypto.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import science.involta.crypto.api.model.Coin
import science.involta.crypto.api.model.HistoryPriceResponse

/**
 * Интерфейс с функциями запроса данных из сети
 */
interface ApiInterface {
    /**
     * Функция для запроса списка криптовалют
     *
     * @param targetCurrency валюта в которой будет указана цена криптовалюты
     */
    @GET("coins/markets")
    suspend fun coinList(@Query("vs_currency") targetCurrency: String): Response<List<Coin>>

    /**
     * Функция для запроса истории изменения цены на конкретную криптовалюту
     *
     * @param id идентификатор криптовалюты
     * @param targetCurrency валюта в которой будет указана цена криптовалюты
     * @param days количество дней за которые требуется получить изменение цены
     */
    @GET("coins/{id}/market_chart")
    suspend fun historicalPrice(
        @Path("id") id: String,
        @Query("vs_currency") targetCurrency: String,
        @Query("days") days: Int
    ): Response<HistoryPriceResponse>
}