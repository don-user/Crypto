package science.involta.crypto.data.repository.coinsList

import science.involta.crypto.api.Result
import science.involta.crypto.api.successed
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.data.local.preferences.PreferenceStorage
import science.involta.crypto.data.local.preferences.SharedPreferencesStorage
import science.involta.crypto.utils.Constants
import java.util.*
import javax.inject.Inject

/**
 * Класс [CoinsListRepository] выполняет роль репозитория при помощи которого
 * ViewModel в дальнейшем будет получать данные
 *
 * @param coinsListRemoteDataSource источник удаленных данных [CoinsListRemoteDataSource]
 * @param coinsListLocalDataSource источник локальных данных [CoinsListLocalDataSource]
 * @param sharedPreferencesStorage источник данных настроек [PreferenceStorage]
 */
class CoinsListRepository @Inject constructor(
    private val coinsListRemoteDataSource: CoinsListRemoteDataSource,
    private val coinsListLocalDataSource: CoinsListLocalDataSource,
    private val sharedPreferencesStorage: PreferenceStorage
) {
    /**
     * Список всех криптовалют
     */
    val allCoinsList = coinsListLocalDataSource.allCoinsList

    /**
     * Обновляние списка криптовалют в базе данных
     *
     * @param targetCur целевая валюта относительно которой мы получим курс
     */
    suspend fun coinsList(targetCur: String) {
        //Запрашиваем данные у удаленного источника данных и выполняем проверку результата
        when (val result = coinsListRemoteDataSource.coinsList(targetCur)) {
            //выполняем если запрос выполнен успешно
            is Result.Success -> {
                //проверяем есть ли в результате данные
                if (result.successed) {
                    //получаем список сокращенных имен для избранных криптовалют
                    //в дальнейшем с его помощью будем устанавливать
                    //флаг в нужное положение
                    val favSymbols = coinsListLocalDataSource.favouriteSymbols()

                    //создаем новый список в который положим обработанные данные
                    val customList = result.data.let {
                        //отфильтровываем объекты у которых сокращенное имя не пустое и не равно null
                        it.filter { item -> item.symbol.isNullOrEmpty().not() }
                            //изменяем объекты (устанавливаем флаг избранного в нужное значение)
                            //далее данные записивыются в список customStockList
                            .map {  item ->
                                CoinsListEntity(
                                    item.symbol ?: "",
                                    item.id,
                                    item.name,
                                    item.price,
                                    item.changePercent,
                                    item.image,
                                    favSymbols.contains(item.symbol)
                                )
                            }
                    }
                    //записываем данные в базу данных
                    coinsListLocalDataSource.insertCoinsIntoDatabase(customList)
                    //обновляем время последнего обновления данных
                    sharedPreferencesStorage.timeLoadedAt = Date().time
                    //возвращаем результат
                    Result.Success(true)
                } else {
                    //в остальных случаях возвращаем ошибку
                    Result.Error(Constants.GENERIC_ERROR)
                }
            }
            //в остальных случаях возвращаем ошибку
            else -> result as Result.Error
        }
    }

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    suspend fun updateFavouriteStatus(symbol: String): Result<CoinsListEntity> {
        val result = coinsListLocalDataSource.updateFavouriteStatus(symbol)
        return result?.let {
            Result.Success(it)
        } ?: Result.Error(Constants.GENERIC_ERROR)
    }

    /**
     * Проверяет требуется ли обновить данные
     */
    fun loadData(): Boolean {
        //получаем время последней загрузки данных
        val lastLoad = sharedPreferencesStorage.timeLoadedAt
        //получаем текущее время
        val currentTime = Date().time
        //проверяем прошло ли 10 секунд
        //1000 (одна тысяча) миллисекунд равняется 1 секунде
        return currentTime - lastLoad > 15 * 1000
    }
}