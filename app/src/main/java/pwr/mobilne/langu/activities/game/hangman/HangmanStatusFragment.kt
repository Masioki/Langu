package pwr.mobilne.langu.activities.game.hangman

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import pwr.mobilne.langu.R


class HangmanStatusFragment : Fragment() {
    private val maxLives: Int = 7
    private var currentLives: Int = 7
    private lateinit var image: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_hangman_image, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = view.findViewById(R.id.imageView)
        if (savedInstanceState != null) {
            currentLives = savedInstanceState.getInt("lives")
        }
        display()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("lives", currentLives)
        super.onSaveInstanceState(outState)
    }

    private fun display() {
        val id =
            resources.getIdentifier(
                "w" + (maxLives - currentLives).toString(),
                "drawable",
                activity?.packageName
            )
        image.setImageResource(id)
    }

    fun reset() {
        currentLives = maxLives
        display()
    }


    fun next() {
        currentLives = 0.coerceAtLeast(currentLives - 1)
        display()
        if (currentLives == 0) {
            (activity as HangmanActivity).lost()
        }
    }
}