package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: WordViewModel
    private lateinit var uvm: WordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uvm = ViewModelProvider(this).get(WordViewModel::class.java)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener{  // TODO pass String array to this activity with key "wordlist"
            val int = Intent(this, WordSearchActivity::class.java)
            int.putExtra("wordlist", arrayListOf("KOCHAM", "APKI", "MOBILNE", "WERI", "MACZ"))
            startActivity(int)
        }

        var germanWord = WordEntity(10,"aa","aaa",1,1)
        uvm.addWord(germanWord)

    }

    fun addFlashcard(view: View){
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        startActivityForResult(intent, 111)
    }
}