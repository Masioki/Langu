package pwr.mobilne.langu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListAdapter
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.InvalidationTracker
import org.jetbrains.annotations.Nullable
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
import pwr.mobilne.langu.activities.game.hangman.HangmanActivity
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsLista:  MutableList<WordEntity>
    private lateinit var categories:  MutableList<String>
    private lateinit var uvm: WordViewModel
    /**
     * onCreate - przy tworzeniu
     */
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            val category = intent?.getStringExtra("category")
            val german = intent?.getStringExtra("german")
            val native = intent?.getStringExtra("native")
            if(german != null && native != null && category != null) {
                val word = WordEntity(0, german, native, Locale.GERMAN, category)
                uvm.addWord(word)
            }
        }
    }
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
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })
        /**
         * PRZYKŁAD DODAWANIA DO BAZY DANYCH
         */
        uvm.addWord(WordEntity(0,"aa","12", Locale.GERMAN,"arh"))

        binding.button.setOnClickListener{  // TODO pass String array to this activity with key "wordlist"
            val int = Intent(this, WordSearchActivity::class.java)
            int.putExtra("wordlist", arrayListOf("KOCHAM", "APKI", "MOBILNE", "WERI", "MACZ"))
            startActivity(int)
        }
    }

    fun addFlashcard(view: View){
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })
        var catArray : Array<String?> = arrayOfNulls(this.categories.size)
        for (i in 0 until this.categories.size){
            catArray[i] = this.categories[i]
        }
        intent.putExtra("categories", catArray)
        //startActivityForResult(intent, 111)
        startForResult.launch(intent)

    }

    fun playHangman(view: View){
        val intent = Intent(this, HangmanActivity::class.java)
        val word = WordEntity(0, "Juni", "June", Locale.GERMAN, "calendar")
        intent.putExtra("word", word)
        startActivity(intent)
    }

}