package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.data.UcWord
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var uvm: UcWord
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        var germanWord = WordEntity(1,"aa","aaa",1,1)
        //uvm.addWord(germanWord) - tu jest bug
    }
    fun addFlashcard(view: View){
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        startActivityForResult(intent, 111)
    }
}