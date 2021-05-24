package pwr.mobilne.langu

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: WordViewModel
    private lateinit var uvm: WordViewModel

    companion object {
        val lang: Locale = Locale.GERMANY
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uvm = ViewModelProvider(this).get(WordViewModel::class.java)
        val view = binding.root
        setContentView(view)
        var germanWord = WordEntity(11,"aa","12",Locale.GERMAN.toString(),"arh")
        uvm.addWord(germanWord)
        Log.e("FDSA1",Locale.GERMANY.country.toString())
        if(Locale.GERMAN == Locale(Locale.GERMAN.toString())) {
            Log.e("AS", Locale(Locale.GERMANY.toString()).toString())
            Log.e("FDSA","abba")
        }
        print("ASDFGH")
        print(uvm.getAllCategories)
        Log.e("FDSeS",Locale.GERMANY.toString())
        Log.e("FDSA","ASDFGH")
    }
}