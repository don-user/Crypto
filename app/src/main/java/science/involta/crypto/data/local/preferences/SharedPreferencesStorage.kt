package science.involta.crypto.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import science.involta.crypto.api.Result
import science.involta.crypto.api.Result.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Интерфейс [PreferenceStorage] содержит ключи SharedPreferences
 */
interface PreferenceStorage {
    //время последнего обновления данных
    var timeLoadedAt: Long
}

/**
 * Класс [SharedPreferencesStorage] управляет SharedPreferences внутри приложения
 */
@Singleton
class SharedPreferencesStorage @Inject constructor(context: Context): PreferenceStorage {
    companion object {
        const val PREFERENCES_NAME = "COINS_PREFS"
        const val PREFERENCES_TIME_LOADED_AT = "PREFS_DATA_LOADED_AT"
    }

    /**
     * Создание объекта SharedPreferences при первом использовании,
     * в дальнейшем будет использоваться ранее созданный объект
     */
    private val preferences: Lazy<SharedPreferences> = lazy {
        context.applicationContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    override var timeLoadedAt by LongPreference(preferences, PREFERENCES_TIME_LOADED_AT, 0)
}


/**
 * Интерфейс [LongPreference] позволяет управлять (читать/записывать) данными типа Long внутри SharedPreferences
 *
 * @param preference объект SharedPreferences
 * @param name ключ хранящегося в SharedPreferences значения
 * @param defaultValue значение по умолчанию, которое вернется в случает отсутствия данных
 */
class LongPreference(
    private val preference: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
): ReadWriteProperty<Any, Long> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preference.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preference.value.edit { putLong(name, value) }
    }
}