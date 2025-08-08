package  com.hook2book.hbsync.RoomDatabase


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    val count: Int,
    val description: String,
    @PrimaryKey val id: Int,
    val categoryName: String,
    val categoryParent: Int
) : java.io.Serializable