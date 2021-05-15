package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.activities.game.hangman.HangmanActivity
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
        val germanWord = WordEntity(1,"konstantynopolitanczykowianeczka","aaa",1,1)

        val int = Intent(this, HangmanActivity::class.java)
        int.putExtra("word", germanWord)
        startActivity(int)
        println("on create started")
        //uvm.addWord(germanWord) - tu jest bug
    }
}