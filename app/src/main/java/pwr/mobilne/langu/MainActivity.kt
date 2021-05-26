package pwr.mobilne.langu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.button.setOnClickListener{  // TODO pass String array to this activity with key "wordlist"
            startActivity(Intent(this, WordSearchActivity::class.java))
        }
    }

}