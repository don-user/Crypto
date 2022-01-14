package science.involta.crypto.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Класс описывающий сущность хранящихся в [CoinsDatabase] элементов
 *
 * @param symbol сокращенное имя криптовалюты
 * @param id идентификатор криптовалюты
 * @param name имя криптовалюты
 * @param price текущая цена криптовалюты
 * @param changePercent изменение стоимости криптовалюты за последние 24 часа
 * @param image ссылка на логотип криптовалюты
 * @param isFavourite флаг указывающий на содержание объекта в избрабнном
 */
@Entity(tableName = "coins_list")
data class CoinsListEntity(
    @PrimaryKey val symbol: String,
    val id: String?,
    val name: String?,
    val price: Double?,
    val changePercent: Double? = null,
    val image: String? = null,
    val isFavourite: Boolean = false
)
