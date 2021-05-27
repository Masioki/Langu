package pwr.mobilne.langu.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun toLaguage(value: String) = Locale(value);

    @TypeConverter
    fun fromLaguage(value: Locale) = value.toString()

}