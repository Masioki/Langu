package pwr.mobilne.langu.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: WordEntity)

    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query("SELECT * FROM words ORDER BY id ASC")
    fun readAllData(): LiveData<List<WordEntity>>

    @Query( "SELECT category FROM words ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<String>>

    @Update
    suspend fun updateWord(word: WordEntity)

}