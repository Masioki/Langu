package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.InvalidationTracker
import org.jetbrains.annotations.Nullable
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import pwr.mobilne.langu.WordSearchActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsLista:  MutableList<WordEntity>
    private lateinit var uvm: WordViewModel

    /**
     * onCreate - przy tworzeniu
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uvm = ViewModelProvider(this).get(WordViewModel::class.java)
        wordsLista = listOf(WordEntity(0,"a1a","13", Locale.ENGLISH,"arh")) as MutableList<WordEntity>

        val view = binding.root
        setContentView(view)
        /**
         * Selectowanie z bazy danych
         */
        uvm.readAllData.observe(this, Observer { status ->
            this.wordsLista= status as MutableList<WordEntity>
        })
        /**
         * PRZYKŁAD DODAWANIA DO BAZY DANYCH
         */
        uvm.addWord(WordEntity(0,"aa","12", Locale.GERMAN,"arh"))

        binding.button.setOnClickListener{
            startWordsearch()
        }
    }

    private fun startWordsearch() {
        // direction:
        // 0: Eng on board, Deutsch displayed
        // 1: Deutsch on board, Eng displayed
        val translationDirection = 1

        val words = wordsLista.toTypedArray()
        words.shuffle()
        val list = words.take(WordsearchConst.SIZE).toTypedArray()
        val map = hashMapOf<String, String>()

        when(translationDirection) {
            0 -> {
                for (word in list){
                    map[word.nativs] = word.german
                }
            }
            1 -> {
                for (word in list){
                    map[word.german] = word.nativs
                }
            }
        }

        val int = Intent(this, WordSearchActivity::class.java)
        int.putExtra("translationMap", map)
        startActivity(int)
    }
}