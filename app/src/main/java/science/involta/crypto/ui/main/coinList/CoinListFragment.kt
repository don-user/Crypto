package science.involta.crypto.ui.main.coinList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_coin_list.*
import science.involta.crypto.R
import science.involta.crypto.adapter.CoinsListAdapter
import science.involta.crypto.adapter.OnItemClickCallback
import science.involta.crypto.common.MainNavigationFragment
import science.involta.crypto.databinding.FragmentCoinListBinding
import science.involta.crypto.ui.chart.ChartActivity
import science.involta.crypto.utils.Constants
import science.involta.crypto.utils.doOnChange

@AndroidEntryPoint
class CoinListFragment: MainNavigationFragment(), OnItemClickCallback {
    private val viewModel: CoinListViewModel by viewModels()
    private lateinit var binding: FragmentCoinListBinding
    private var coinListAdapter = CoinsListAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCoinListBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@CoinListFragment.viewModel
            }
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        viewModel.loadCoinsFromApi()
    }

    override fun initializeViews() {
        coinsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coinListAdapter
        }
    }

    /**
     * Функция внутри которой выполняется подписка на обновление объектов LiveData
     */
    override fun observeViewModel() {
        /**
         * Подписываемся на обновление статуса работы с сетью.
         * Если в данный момент выполняется загрузка отображаем ProgressBar.
         */
        viewModel.isLoading.doOnChange(this) { loading ->
            coinsListLoading.visibility = if (viewModel.isListEmpty() && loading) View.VISIBLE else View.GONE

            if (loading) coinsListErrorView.visibility = View.GONE
        }

        /**
         * Подписываемся на обновление списка криптовалют.
         * При обновлении передаем данные в [CoinsListAdapter],
         * а также проверяем список на пустоту, и в зависимости
         * от результата показываем на экране сообщение об ошибке.
         */
        viewModel.coinsListData.doOnChange(this) {
            it.forEach { p -> println("${p.name}") }
            coinListAdapter.updateList(it)

            coinsListErrorView.visibility = if (viewModel.isListEmpty()) View.VISIBLE else View.GONE
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

        /**
         * Полписываемся на сообщения об ошибке,
         * при появлении сообщения отображаем Toast с текстом ошибки
         */
        viewModel.toastError.doOnChange(this) {
            showToast(it)
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