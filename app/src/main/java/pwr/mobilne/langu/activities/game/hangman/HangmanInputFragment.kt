package pwr.mobilne.langu.activities.game.hangman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
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
    private lateinit var endLayout: LinearLayout
    private var locked: Boolean = false
    private var guessedLetters: MutableList<Char> = mutableListOf()
    private var letterContainers: MutableList<View> = mutableListOf()
    private lateinit var letters: CharArray

    constructor(word: WordEntity) : this() {
        this.word = word
        var s = ""
        val set = com.ibm.icu.util.LocaleData.getExemplarSet(
            com.ibm.icu.util.ULocale.forLocale(word.laguage),
            com.ibm.icu.util.LocaleData.ES_STANDARD
        )
        for (i in set) {
            s += i.toString().uppercase()
        }
        letters = s.toCharArray()
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
        endLayout = view.findViewById(R.id.endLayout)
        view.findViewById<TextView>(R.id.againButton).setOnClickListener {
            (activity as HangmanActivity).start()
        }

        if (savedInstanceState != null) {
            word = savedInstanceState.getSerializable("word") as WordEntity
            letterContainers =
                (savedInstanceState.getSerializable("containers") as Array<View>).toMutableList()
            guessedLetters =
                (savedInstanceState.getSerializable("guessed") as Array<Char>).toMutableList()
            locked = savedInstanceState.getBoolean("locked")
            letters = savedInstanceState.getSerializable("letters") as CharArray
        }

        reset()
        update()
        checkWin()
        if (locked) displayDescription()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("word", word)
        outState.putSerializable("containers", letterContainers.toTypedArray())
        outState.putSerializable("guessed", guessedLetters.toTypedArray())
        outState.putBoolean("locked", locked)
        outState.putSerializable("letters", letters)
    }

    private fun reset() {
        keyboardLayout.removeAllViews()
        for (letter in letters) {
            val key = layoutInflater.inflate(R.layout.hangman_input_key, keyboardLayout, false)
            key.setOnClickListener {
                play(letter)
                key.setBackgroundResource(R.drawable.hangman_key_background_inactive)
                key.elevation = 2F
                // key.background = resources.getDrawable(R.drawable.hangman_key_background_inactive)
            }
            if (guessedLetters.contains(letter)) {
                key.setBackgroundResource(R.drawable.hangman_key_background_inactive)
            }
            key.findViewById<TextView>(R.id.hangmanKey).text = letter.toString()
            keyboardLayout.addView(key)
        }
        lettersLayout.removeAllViews()
        letterContainers.clear()
        for (i in 1..word.german.length) {
            val container =
                layoutInflater.inflate(R.layout.hangman_input_letter, lettersLayout, false)
            lettersLayout.addView(container)
            letterContainers.add(container)
        }

        endLayout.visibility = INVISIBLE
        description.text = word.nativs
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

    private fun checkWin(): Boolean {
        if (guessedLetters.containsAll(
                word.german.toCharArray().toList().stream()
                    .map { i -> i.uppercaseChar() }
                    .collect(Collectors.toList()))
        ) {
            println("check win")
            displayDescription()
            (activity as HangmanActivity).win()
            lock()
            return true
        }
        return false
    }

    private fun play(letter: Char) {
        if (!guessedLetters.contains(letter) && !locked) {
            guessedLetters.add(letter)
            if (!checkWin()) {
                if (indexOf(letter) < 0) {
                    (activity as HangmanActivity).missed()
                }
            }
            update()
        }
    }

    fun playAgain() {

    }

    fun lock() {
        locked = true
    }

    fun displayDescription() {
        endLayout.visibility = VISIBLE
        endLayout.alpha = 0F
        endLayout.animate()
            .setDuration(1500)
            .translationY(-endLayout.height.toFloat() / 2.5F)
            .alpha(1.0F)
            .setListener(null);
    }
}