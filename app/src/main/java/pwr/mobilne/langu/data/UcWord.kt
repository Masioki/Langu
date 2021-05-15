package pwr.mobilne.langu.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UcWord(application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<WordEntity>>
    private val repository: WordRepository

    init {
        val userDao = WordDatabase.getDatabase(application).userDao()
        repository = WordRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addWord(task: WordEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWord(task)
        }
    }

    fun deleteWord(task: WordEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWord(task)
        }
    }

    fun updateWord(task: WordEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWord(task)
        }
    }

}