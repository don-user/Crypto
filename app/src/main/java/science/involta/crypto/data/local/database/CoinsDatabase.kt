package science.involta.crypto.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import science.involta.crypto.data.local.preferences.SharedPreferencesStorage
import science.involta.crypto.utils.Constants.DATABASE_NAME
import science.involta.crypto.utils.Constants.DATABASE_VERSION

/**
 * Класс [CoinsDatabase] управляет базой данных Room внутри приложения
 */
@Database(entities = [CoinsListEntity::class], version = DATABASE_VERSION, exportSchema = false)
abstract class CoinsDatabase: RoomDatabase() {
    abstract fun coinsListDao(): CoinsListDao

    companion object {
        /**
         * Функция для сборки базы данных
         */
        fun buildDatabase(context: Context): CoinsDatabase {
            return Room.databaseBuilder(context, CoinsDatabase::class.java, DATABASE_NAME).build()
        }
    }
}