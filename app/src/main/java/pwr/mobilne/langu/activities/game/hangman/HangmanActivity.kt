package pwr.mobilne.langu.activities.game.hangman

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.R
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.databinding.ActivityHangmanBinding

class HangmanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHangmanBinding
    private lateinit var status: HangmanStatusFragment
    private lateinit var input: HangmanInputFragment
    private lateinit var word: WordEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHangmanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState != null) {
            word = savedInstanceState.getSerializable("word") as WordEntity
            status = supportFragmentManager.getFragment(
                savedInstanceState,
                "status"
            ) as HangmanStatusFragment
            input = supportFragmentManager.getFragment(
                savedInstanceState,
                "input"
            ) as HangmanInputFragment
        } else {
            word = intent.getSerializableExtra("word") as WordEntity
            status = HangmanStatusFragment()
            input = HangmanInputFragment(word)
        }
        supportFragmentManager.beginTransaction().add(R.id.hangmanStatusContainer, status).commit()
        supportFragmentManager.beginTransaction().add(R.id.hangmanInputContainer, input).commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("word", word)
        supportFragmentManager.putFragment(outState, "status", status)
        supportFragmentManager.putFragment(outState, "input", input)
    }

    fun win() {
        Toast.makeText(this, "win", LENGTH_LONG).show()
    }

    fun missed() {
        status.next()
    }
}