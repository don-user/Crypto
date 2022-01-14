package science.involta.crypto

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Все приложения используещие Hilt должны содержать класс наследуемый от Application
 * Аннотация @HiltAndroidApp запускает генерацию кода Hilt, включая базовый класс вашего приложения.
 * После генерации данный класс станет контейнером для зависимостей на уровне приложения.
 */
@HiltAndroidApp
class App: Application()