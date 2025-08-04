import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hook2book.hbsync.RoomDatabase.CategoriesDao
import com.hook2book.hbsync.RoomDatabase.CategoriesEntity

@Database(entities = [CategoriesEntity::class], version = 3)
abstract class CommonDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao

    companion object {
        @Volatile
        private var INSTANCE: CommonDatabase?= null
        fun getDatabase(context: Context): CommonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommonDatabase::class.java,
                    "common_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}