package science.involta.crypto.api.model

import com.google.gson.annotations.SerializedName

/**
 * Класс описывающий модель для данных о криптовалютах которые будут получены из сети
 *
 * @param symbol сокращенное имя криптовалюты
 * @param id идентификатор криптовалюты
 * @param name имя криптовалюты
 * @param image ссылка на логотип криптовалюты
 * @param price текущая цена криптовалюты
 * @param changePercent изменение стоимости криптовалюты за последние 24 часа
 */
data class Coin(
    val symbol: String?,
    val id: String?,
    val name: String?,
    val image: String?,
    @SerializedName("current_price") val price: Double?,
    @SerializedName("price_change_percentage_24h") val changePercent: Double?
)
