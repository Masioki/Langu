package pwr.mobilne.langu.activities.game.hangman

import android.app.ActionBar
import android.content.res.Configuration
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.R
import pwr.mobilne.langu.activities.MainActivity
import pwr.mobilne.langu.data.WordDatabase
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



        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.hangmanLayout.orientation = LinearLayout.HORIZONTAL
            binding.hangmanInputContainer.layoutParams.width = 0
            binding.hangmanInputContainer.layoutParams.height = ActionBar.LayoutParams.MATCH_PARENT
            binding.hangmanStatusContainer.layoutParams.width = 0
            binding.hangmanStatusContainer.layoutParams.height = ActionBar.LayoutParams.MATCH_PARENT
        } else {
            binding.hangmanLayout.orientation = LinearLayout.VERTICAL
            binding.hangmanInputContainer.layoutParams.width = ActionBar.LayoutParams.MATCH_PARENT
            binding.hangmanInputContainer.layoutParams.height = 0
            binding.hangmanStatusContainer.layoutParams.width = ActionBar.LayoutParams.MATCH_PARENT
            binding.hangmanStatusContainer.layoutParams.height = 0
        }


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
            //word = intent.getSerializableExtra("word") as WordEntity
            word = WordDatabase.getDatabase(this).userDao().getRandomWord(MainActivity.appLanguage)
            status = HangmanStatusFragment()
            input = HangmanInputFragment(word)
            supportFragmentManager.beginTransaction().add(R.id.hangmanStatusContainer, status)
                .commit()
            supportFragmentManager.beginTransaction().add(R.id.hangmanInputContainer, input)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("word", word)
        supportFragmentManager.putFragment(outState, "status", status)
        supportFragmentManager.putFragment(outState, "input", input)
    }

    fun start() {
        supportFragmentManager.beginTransaction().remove(status).commit()
        supportFragmentManager.beginTransaction().remove(input).commit()
        word = WordDatabase.getDatabase(this).userDao().getRandomWord(MainActivity.appLanguage)
        status = HangmanStatusFragment()
        input = HangmanInputFragment(word)
        supportFragmentManager.beginTransaction().add(R.id.hangmanStatusContainer, status)
            .commit()
        supportFragmentManager.beginTransaction().add(R.id.hangmanInputContainer, input)
            .commit()
    }

    fun win() {
        Toast.makeText(this, "win", LENGTH_LONG).show()
    }

    fun lost() {
        Toast.makeText(this, "lose", LENGTH_LONG).show()
        input.lock()
        input.displayDescription()
    }

    fun missed() {
        status.next()
    }
}