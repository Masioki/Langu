package pwr.mobilne.langu.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pwr.mobilne.langu.R
import pwr.mobilne.langu.activities.game.hangman.HangmanActivity
import pwr.mobilne.langu.activities.game.wordsearch.WordSearchActivity
import pwr.mobilne.langu.activities.game.wordsearch.WordsearchConst
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsLista: MutableList<WordEntity>
    private lateinit var categories: MutableList<String>
    private lateinit var uvm: WordViewModel

    /**
     * onCreate - przy tworzeniu
     */
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val category = intent?.getStringExtra("category")
                val german = intent?.getStringExtra("german")
                val native = intent?.getStringExtra("native")
                val language = intent?.getSerializableExtra("language")
                if (german != null && native != null && category != null) {
                    val word = WordEntity(0, german, native, language as Locale, category)
                    uvm.addWord(word)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uvm = ViewModelProvider(this).get(WordViewModel::class.java)
        wordsLista =
            listOf(WordEntity(0, "a1a", "13", Locale.ENGLISH, "arh")) as MutableList<WordEntity>

        val view = binding.root
        setContentView(view)
        /**
         * Selectowanie z bazy danych
         */
        uvm.readAllData.observe(this, Observer { status ->
            this.wordsLista = status as MutableList<WordEntity>
        })
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })
        /**
         * PRZYKŁAD DODAWANIA DO BAZY DANYCH
         */
        uvm.addWord(WordEntity(0, "aaghg", "1hgh2", Locale.GERMAN, "arh"))

        binding.button.setOnClickListener {
            startWordsearch()
        }

        val btnAdd: TextView? = findViewById(R.id.buttonAdd)
        val btnSearch: TextView? = findViewById(R.id.button)
        val btnHangman: TextView? = findViewById(R.id.buttonHangman)
        val moveUp = TranslateAnimation(0F, 0F, 400F, 0F)
        moveUp.setDuration(1000)
        moveUp.setFillAfter(true)
        btnAdd?.startAnimation(moveUp)
        btnSearch?.startAnimation(moveUp)
        btnHangman?.startAnimation(moveUp)

    }
    private fun startWordsearch() {
        // direction:
        // 0: nativs on board, Deutsch displayed
        // 1: Deutsch on board, nativs displayed
        val translationDirection = (0..1).random()

        val words = wordsLista.toTypedArray()
        val language = words[0].laguage
        // shuffle the list ant take a number of first elements
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
        int.putExtra("language", language)
        startActivity(int)
    }

    fun addFlashcard(view: View) {
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })
        var catArray: Array<String?> = arrayOfNulls(this.categories.size)
        for (i in 0 until this.categories.size) {
            catArray[i] = this.categories[i]
        }
        intent.putExtra("categories", catArray)
        //startActivityForResult(intent, 111)
        startForResult.launch(intent)

    }

    fun playHangman(view: View) {
        val intent = Intent(this, HangmanActivity::class.java)
        val word = WordEntity(0, "Juni", "June", Locale.GERMAN, "calendar")
        intent.putExtra("word", word)
        startActivity(intent)
    }

}