package science.involta.crypto.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Интерфейс [CoinsListDao] содержащий функции-запросы к базе данных [CoinsDatabase]
 */
@Dao
interface CoinsListDao {
    /**
     * Функция для получения из базы данных всего её содержимого, т.е. полного списка криптовалют
     */
    @Query("SELECT * FROM coins_list")
    fun coinsList(): LiveData<List<CoinsListEntity>>

    /**
     * Функция для получения из базы данных конкретной криптовалюты по её сокращенному имени
     */
    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    suspend fun coinFromSymbol(symbol: String): CoinsListEntity?

    /**
     * Функция для получения из базы данных конкретной криптовалюты по её сокращенному имени
     */
    @Query("SELECT * FROM coins_list WHERE symbol = :symbol")
    fun coinLiveDataFromSymbol(symbol: String): LiveData<CoinsListEntity>

    /**
     * Функция для получения из базы данных списка избранных криптовалют
     */
    @Query("SELECT * FROM coins_list WHERE isFavourite = 1")
    fun favouriteCoins(): LiveData<List<CoinsListEntity>>

    /**
     * Функция для получения из базы данных списка сокращенных имен всех избранных криптовалют
     */
    @Query("SELECT symbol FROM coins_list WHERE isFavourite = 1")
    suspend fun favouriteSymbols(): List<String>

    /**
     * Записывает данные в базу. Если какой-то объект уже имеется, он перезаписывается.
     *
     * @param list список объектов [CoinsListEntity] которые необходимо записать в базу данных
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<CoinsListEntity>)

    /**
     * Обновляет конкретный объект внутри базы данных
     *
     * @param data объект [CoinsListEntity] который необходимо обновить
     */
    @Update
    suspend fun updateCoinsListEntity(data: CoinsListEntity): Int

    /**
     * Функция для удаления всех элементов из базы данных
     */
    @Query("DELETE FROM coins_list")
    suspend fun deleteAll()
}