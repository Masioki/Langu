package pwr.mobilne.langu.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWord(word: WordEntity)

    @Delete
    suspend fun deleteWord(word: WordEntity)

    @Query("SELECT * FROM words ORDER BY id ASC")
    fun readAllData(): LiveData<List<WordEntity>>

    @Query( "SELECT DISTINCT category FROM words ORDER BY category ASC")
    fun getAllCategories(): LiveData<List<String>>

    @Update
    suspend fun updateWord(word: WordEntity)

    @Query("SELECT  * FROM words WHERE laguage = :lang ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(lang: String): WordEntity

}