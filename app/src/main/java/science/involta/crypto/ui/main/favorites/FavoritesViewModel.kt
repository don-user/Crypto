package science.involta.crypto.ui.main.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import science.involta.crypto.api.Result
import science.involta.crypto.data.local.database.CoinsListEntity
import science.involta.crypto.data.repository.favourites.FavoritesRepository
import javax.inject.Inject

/**
 * ViewModel для фрагмента [FavouritesFragment] с ее помощью фрагмент будет получать данные
 *
 * @param repository репозиторий [FavoritesRepository] из которого ViewModel получает данные
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: FavoritesRepository): ViewModel() {
    /**
     * [_toastError] хранит сообщние об ошибке в случае ее возникновения
     * [toastError] позволяет получить сообщение об ошибке из View [FavouritesFragment]
     */
    private val _toastError = MutableLiveData<String>()
    val toastError: LiveData<String> = _toastError

    /**
     * [favoriteCoinsList] хранит полный список избранных криптовалют
     */
    val favoriteCoinsList: LiveData<List<CoinsListEntity>> = repository.favoriteCoins

    /**
     * [_favoriteStock] хранит объект изменяемой криптовалюты
     * [favoriteStock] позволяет получить объект изменяемой криптовалюты из View [FavouritesFragment]
     */
    private val _favoriteStock = MutableLiveData<CoinsListEntity?>()
    val favoriteStock: LiveData<CoinsListEntity?> = _favoriteStock

    /**
     * [_favoritesEmpty] хранит информацию о пустоте списка избранных криптовалют
     * [favoritesEmpty] позволяет получить информацию о пустоте
     * списка избранных криптовалют из View [FavouritesFragment]
     */
    private val _favoritesEmpty = MutableLiveData<Boolean>()
    val favoritesEmpty: LiveData<Boolean> = _favoritesEmpty

    /**
     * Обновляет статус нахождения в избранном для конкретной криптовалюты
     *
     * @param symbol сокращенное имя криптовалюты
     */
    fun updateFavoriteStatus(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFavoriteStatus(symbol)
            when (result) {
                is Result.Success -> _favoriteStock.postValue(result.data)
                is Result.Error -> _toastError.postValue(result.message)
            }
        }
    }

    /**
     * Позволяет изменить информацию о пустоте списка избранных криптовалют
     *
     * @param empty значение true(список пуст)/false (список не пуст)
     */
    fun isFavoriteEmpty(empty: Boolean) {
        _favoritesEmpty.postValue(empty)
    }
}