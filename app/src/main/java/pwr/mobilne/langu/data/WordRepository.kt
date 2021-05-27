package pwr.mobilne.langu.data

import androidx.lifecycle.LiveData

class WordRepository(private val taskDao: WordDao) {
    val readAllData: LiveData<List<WordEntity>> = taskDao.readAllData()
    val getAllCategories: LiveData<List<String>> = taskDao.getAllCategories()

    suspend fun addWord(user: WordEntity) {
        taskDao.addWord(user)
    }

    suspend fun deleteWord(user: WordEntity) {
        taskDao.deleteWord(user)
    }

    suspend fun updateWord(task: WordEntity) {
        taskDao.updateWord(task)
    }
}