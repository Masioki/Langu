package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pwr.mobilne.langu.activities.game.hangman.HangmanActivity
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsLista: MutableList<WordEntity>
    private lateinit var uvm: WordViewModel

    /**
     * onCreate - przy tworzeniu
     */
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
        /**
         * PRZYKŁAD DODAWANIA DO BAZY DANYCH
         */
        uvm.addWord(WordEntity(0, "aaghg", "1hgh2", Locale.GERMAN, "arh"))

        binding.button.setOnClickListener {  // TODO pass String array to this activity with key "wordlist"
            val int = Intent(this, WordSearchActivity::class.java)
            int.putExtra("wordlist", arrayListOf("KOCHAM", "APKI", "MOBILNE", "WERI", "MACZ"))
            startActivity(int)
        }
        val w = WordEntity(1, "german", "opis opis", Locale.GERMAN, "kat")
        uvm.updateWord(w)

        val int = Intent(this, HangmanActivity::class.java)
        int.putExtra("word", w)
        startActivity(int)
    }
}