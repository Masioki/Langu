package pwr.mobilne.langu.activities.game.hangman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexboxLayout
import pwr.mobilne.langu.R
import pwr.mobilne.langu.data.WordEntity
import java.util.stream.Collectors


class HangmanInputFragment() : Fragment() {
    private lateinit var keyboardLayout: FlexboxLayout
    private lateinit var lettersLayout: FlexboxLayout
    private lateinit var description: TextView
    private lateinit var word: WordEntity
    private val guessedLetters: MutableList<Char> = mutableListOf()
    private val letterContainers: MutableList<View> = mutableListOf()
    private val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    constructor(word: WordEntity) : this() {
        this.word = word
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_hangman_input, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyboardLayout = view.findViewById(R.id.keyboardLayout)
        lettersLayout = view.findViewById(R.id.wordLayout)
        description = view.findViewById(R.id.wordDescription)

        for (letter in letters) {
            val key = layoutInflater.inflate(R.layout.hangman_input_key, keyboardLayout, false)
            key.setOnClickListener {
                play(letter)
                key.setBackgroundResource(R.drawable.hangman_key_background_inactive)
                // key.background = resources.getDrawable(R.drawable.hangman_key_background_inactive)
            }
            key.findViewById<TextView>(R.id.hangmanKey).text = letter.toString()
            keyboardLayout.addView(key)
        }
        for (i in 1..word.german.length) {
            val container =
                layoutInflater.inflate(R.layout.hangman_input_letter, lettersLayout, false)
            lettersLayout.addView(container)
            letterContainers.add(container)
        }

        if (savedInstanceState != null) {
            word = savedInstanceState.getSerializable("word") as WordEntity
        }
        description.visibility= INVISIBLE
        description.text = word.nativs
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("word", word)
        outState.putSerializable("containers", letterContainers.toTypedArray())
        outState.putSerializable("guessed", guessedLetters.toTypedArray())
    }

    private fun indexOf(letter: Char): Int {
        if (word.german.contains(letter, true))
            return word.german.indexOf(letter, 0, true)
        return -1
    }

    private fun update() {
        for (i in word.german.toCharArray().indices) {
            if (guessedLetters.contains(word.german.uppercase()[i])) {
                letterContainers[i].findViewById<TextView>(R.id.hangmanLetter).text =
                    word.german.uppercase()[i].toString()
            }
        }
    }

    private fun play(letter: Char) {
        if (!guessedLetters.contains(letter)) {
            guessedLetters.add(letter)
            if (guessedLetters.containsAll(
                    word.german.toCharArray().toList().stream()
                        .map { i -> i.uppercaseChar() }
                        .collect(Collectors.toList()))
            ) {
                description.visibility = VISIBLE
                description.alpha = 0F
                description.animate()
                    .setDuration(1500)
                    .translationY(-description.height.toFloat())
                    .alpha(1.0F)
                    .setListener(null);
                (activity as HangmanActivity).win()
            } else if (indexOf(letter) < 0) {
                (activity as HangmanActivity).missed()
            }
            update()
        }
    }

}