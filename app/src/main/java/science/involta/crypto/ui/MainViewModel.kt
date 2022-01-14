package science.involta.crypto.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel для класса [MainActivity] использовать не будем,
 * создаем для целостности приложения с точки зрения подхода
 */
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel()