package pwr.mobilne.langu.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: WordEntity)

    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query( "SELECT * FROM words ORDER BY id ASC")
    fun readAllData(): LiveData<List<WordEntity>>

    @Update
    suspend fun updateWord(word: WordEntity)

}