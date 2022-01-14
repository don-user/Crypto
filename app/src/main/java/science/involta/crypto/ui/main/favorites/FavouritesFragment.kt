package science.involta.crypto.ui.main.favorites

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_coin_list.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import science.involta.crypto.R
import science.involta.crypto.adapter.CoinsListAdapter
import science.involta.crypto.adapter.OnItemClickCallback
import science.involta.crypto.common.MainNavigationFragment
import science.involta.crypto.databinding.FragmentCoinListBinding
import science.involta.crypto.databinding.FragmentFavouritesBinding
import science.involta.crypto.ui.chart.ChartActivity
import science.involta.crypto.ui.main.coinList.CoinListViewModel
import science.involta.crypto.utils.Constants
import science.involta.crypto.utils.doOnChange

@AndroidEntryPoint
class FavouritesFragment: MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var binding: FragmentFavouritesBinding
    private var favoritesAdapter = CoinsListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@FavouritesFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun initializeViews() {
        favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = favoritesAdapter
        }
    }

    /**
     * Функция внутри которой выполняется подписка на обновление объектов LiveData
     */
    override fun observeViewModel() {
        /**
         * Подписываемся на обновление списка избранных криптовалют,
         * при обновлении передаем данные в объект [CoinsListAdapter].
         * Выполняем проверку списка на пустоту, и обновляем
         * значение внутри [FavoritesViewModel].
         */
        viewModel.favoriteCoinsList.doOnChange(this) {
            favoritesAdapter.updateList(it)

            if (it.isEmpty()) {
                viewModel.isFavoriteEmpty(true)
            }
        }

        /**
         * Полписываемся на сообщения об ошибке,
         * при появлении сообщения отображаем Toast с текстом ошибки
         */
        viewModel.toastError.doOnChange(this) {
            showToast(it)
        }

        /**
         * Подписываемся на изменение объектов добавляемых/удаляемых из избранного
         * при обновлении отображаем Toast с соответствующей информацией
         */
        viewModel.favoriteStock.doOnChange(this) {
            it?.let {
                showToast(
                    if (it.isFavourite)
                        "${it.symbol} добавлен в избранное"
                    else
                        "${it.symbol} удален из избранного"
                )
            }
        }
    }

    /**
     * Обрабатываем нажатие на элемент из RecyclerView, по нажатию
     * открываем [ChartActivity] и передаем в него параметры принимаемые функцией
     *
     * @param symbol сокращенное имя криптовалюты
     * @param id индентификатор криптовалюты
     */
    override fun onItemClick(symbol: String, id: String) {
        requireActivity().run {
            startActivity(
                Intent(this, ChartActivity::class.java)
                    .apply {
                        putExtra(Constants.EXTRA_SYMBOL, symbol)
                        putExtra(Constants.EXTRA_SYMBOL_ID, id)
                    }
            )
        }
    }

    /**
     * Обрабатываем нажатие на иконку добавления/удаления криптовалюты из избранного
     *
     * @param symbol сокращенное имя криптовалюты
     */
    override fun onFavoriteClick(symbol: String) {
        viewModel.updateFavoriteStatus(symbol)
    }

    /**
     * Отображает на экране Toast
     *
     * @param message текст сообщения
     * @param duration время показа сообщения на экране
     */
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}