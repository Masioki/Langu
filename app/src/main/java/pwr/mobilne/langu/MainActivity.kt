package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListAdapter
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.InvalidationTracker
import org.jetbrains.annotations.Nullable
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import androidx.lifecycle.Observer
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

        binding.button.setOnClickListener{  // TODO pass String array to this activity with key "wordlist"
            val int = Intent(this, WordSearchActivity::class.java)
            int.putExtra("wordlist", arrayListOf("KOCHAM", "APKI", "MOBILNE", "WERI", "MACZ"))
            startActivity(int)
        }
    }

    // TODO: pass distinct categories as String Array as StringExtra
    fun addFlashcard(view: View){
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        /* TODO:
                val categories : Array<String> = uvm.getCategories()
                intent.putExtra("categories", categories)
         */
        startActivityForResult(intent, 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        if(resultCode == 111) {
            super.onActivityResult(requestCode, resultCode, intentData)
            val category = intentData?.getStringExtra("category")
            val german = intentData?.getStringExtra("german")
            val native = intentData?.getStringExtra("native")
            if(german != null && native != null && category != null) {
                /* TODO:
                        val categories : Array<String> = uvm.getCategories()
                        if(category in categories){
                            val catId = uvm.getCategoryId(category)
                        }
                        else{
                            uvm.insertCategory(category);
                        }
                 */
                 val catId = 0

                val word: WordEntity = WordEntity(0, german, native, 1, catId)
                uvm.addWord(word)
            }
        }
    }
}