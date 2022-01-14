package science.involta.crypto.ui.main.coinList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import science.involta.crypto.api.Result
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.data.repository.coinsList.CoinsListRepository
import science.involta.crypto.ui.main.favorites.FavouritesFragment
import javax.inject.Inject

/**
 * ViewModel для фрагмента [CoinListFragment] с ее помощью фрагмент будет получать данные
 *
 * @param repository репозиторий [CoinsListRepository] из которого ViewModel получает данные
 */
@HiltViewModel
class CoinListViewModel @Inject constructor(private val repository: CoinsListRepository): ViewModel() {
    /**
     * [_isLoading] хранит изменяемую с статусом загрузки данных из сети
     * [isLoading] позволяет получить доступ к статусу из View [CoinListFragment]
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * [_toastError] хранит сообщние об ошибке в случае ее возникновения
     * [toastError] позволяет получить сообщение об ошибке из View [CoinListFragment]
     */
    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    /**
     * [coinsListData] хранит полный список криптовалют
     */
    val coinsListData = repository.allCoinsList

    /**
     * [_favoriteStock] хранит объект изменяемой криптовалюты
     * [favoriteStock] позволяет получить объект изменяемой криптовалюты из View [FavouritesFragment]
     */
    private val _favoriteStock = MutableLiveData<CoinsListEntity?>()
    val favoriteStock: LiveData<CoinsListEntity?> = _favoriteStock

    /**
     * Проверяет пуст ли список [coinsListData]
     */
    fun isListEmpty(): Boolean {
        return coinsListData.value?.isEmpty() ?: true
    }

    /**
     * Запрашивает данные из сети
     *
     * @param targetCur валюта в которой мы хотим увидить курс криптовалюты
     */
    fun loadCoinsFromApi(targetCur: String = "usd") {
        if (repository.loadData()) {
            viewModelScope.launch(Dispatchers.IO) {
                _isLoading.postValue(true)
                repository.coinsList(targetCur)
                _isLoading.postValue(false)
            }
        }
    }

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun updateFavoriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFavouriteStatus(symbol)
            when (result) {
                is Result.Success -> _favoriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }
}