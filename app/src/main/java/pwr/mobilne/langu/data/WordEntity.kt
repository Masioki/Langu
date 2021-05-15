package pwr.mobilne.langu.data
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable

@Entity(tableName = "words")
data class WordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val german: String,

    val nativs: String,

    val laguage: Int,

    var category: Int


) : Serializable