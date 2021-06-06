package pwr.mobilne.langu

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.RelativeLayout
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity

object WordsearchConst {
    const val SIZE = 12 // board size
}


class WordSearchActivity : AppCompatActivity() {

    private lateinit var lettersGrid: Array<Array<String?>>
    private lateinit var detectionGrid: Array<Array<Int?>>
    private lateinit var surfaceView: DrawingView
    private lateinit var tableLayout: TableLayout
    private lateinit var wordsTextView: TextView

    private lateinit var translationMap: HashMap<String, String>
    private lateinit var wordList: Array<String>
    private lateinit var foundWords: MutableList<String>
    private lateinit var foundCoords: MutableList<IntArray>
    private lateinit var wordsPlaced: List<Int>

    private var SIZE = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_search)
        SIZE = WordsearchConst.SIZE

        @Suppress("UNCHECKED_CAST")
        translationMap =
            (intent.getSerializableExtra("translationMap") as HashMap<String, String>?)!!
        wordList = translationMap.keys.filter { it.length <= SIZE }.toTypedArray()
        wordsTextView = findViewById(R.id.word_list)
        buildGrid()
        foundWords = mutableListOf()
        foundCoords = mutableListOf()
//        wordList =
//            intent.getStringArrayListExtra("wordlist")!!.filter { it.length <= SIZE }.toTypedArray()
        wordsPlaced = createWordSearch(wordList, SIZE) // get indices of successfully placed words
        addRandomLetters()
        printGrid()
        setWordListText()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("lettersGrid", lettersGrid)
        outState.putSerializable("detectionGrid", detectionGrid)
        outState.putSerializable("foundWords", foundWords.toTypedArray())
        outState.putSerializable("foundCoords", foundCoords.toTypedArray())
        outState.putSerializable("wordsPlaced", wordsPlaced.toTypedArray())
        outState.putSerializable("translationMap", translationMap)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // get arrays from bundle and cast them properly
        var tmplist = savedInstanceState.getSerializable("lettersGrid") as Array<*>
        lettersGrid =
            tmplist.filterIsInstance<Array<String?>>().takeIf { it.size == tmplist.size }!!
                .toTypedArray()

        tmplist = savedInstanceState.getSerializable("detectionGrid") as Array<*>
        detectionGrid = tmplist.filterIsInstance<Array<Int?>>().takeIf { it.size == tmplist.size }!!
            .toTypedArray()

        tmplist = savedInstanceState.getSerializable("foundWords") as Array<*>
        foundWords =
            tmplist.filterIsInstance<String>().takeIf { it.size == tmplist.size }!!.toMutableList()

        tmplist = savedInstanceState.getSerializable("wordsPlaced") as Array<*>
        wordsPlaced = tmplist.filterIsInstance<Int>().takeIf { it.size == tmplist.size }!!

        tmplist = savedInstanceState.getSerializable("foundCoords") as Array<*>
        foundCoords = tmplist.filterIsInstance<IntArray>().takeIf { it.size == tmplist.size }!!
            .toMutableList()

        @Suppress("UNCHECKED_CAST")  // dont know how to cast to hashmap :<
        translationMap =
            (savedInstanceState.getSerializable("translationMap") as HashMap<String, String>?)!!

        // restore colors
        for (e in foundCoords) {
            markFoundWord(e[0], e[1], e[2], e[3])
        }
        printGrid()
        setWordListText()
    }


    private fun inViewInBounds(view: View, x: Int, y: Int): Boolean {
        val outRect = Rect()
        val location = IntArray(2)

        view.getDrawingRect(outRect)
        view.getLocationOnScreen(location)
        outRect.offset(location[0], location[1])
        return outRect.contains(x, y)
    }

    fun registerMove(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int) {
        var start: TextView? = null
        var end: TextView? = null
        var startIndex: Int? = null
        var endIndex: Int? = null
        val startingPos = IntArray(2)
        val endingPos = IntArray(2)
        outer@ for (row in 0 until SIZE) {
            for (col in 0 until SIZE) {
                val textViewCell = getTableLayoutCell(tableLayout, row, col) as TextView
                if (inViewInBounds(textViewCell, xStart, yStart)) {
                    start = textViewCell
                    startIndex = detectionGrid[row][col]
                    startingPos[0] = row
                    startingPos[1] = col
                }
                if (inViewInBounds(textViewCell, xEnd, yEnd)) {
                    end = textViewCell
                    endIndex = detectionGrid[row][col]
                    endingPos[0] = row
                    endingPos[1] = col
                }
                if (start != null && end != null) {
                    break@outer
                }
            }
        }
        if (startIndex == endIndex && startIndex != null && start != end) {
            val word = wordList[startIndex]
            if (word !in foundWords) {
                foundWords.add(word)

                val direction: Int = if (startingPos[0] == endingPos[0]) // same row
                    0
                else if (startingPos[1] == endingPos[1])  // same col
                    1
                else  // diagonally
                    2
                foundCoords.add(intArrayOf(startingPos[0], startingPos[1], word.length, direction))
                markFoundWord(startingPos[0], startingPos[1], word.length, direction = direction)
                println("You found word : $word - ${translationMap[word]}!")
                setWordListText()
                if (wordList.size == foundWords.size) {
                    endDialog()
                }
            }
        }
    }

    private fun endDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Spiel ist aus!")
        val message = "Glückwunsch, du hast gewonnen"
        builder.setMessage(message)
        builder.setPositiveButton("OK") { _, _ -> finish() }
        builder.show()
    }

    private fun setWordListText() {
        val bobTheBuilder = SpannableStringBuilder()
        var numElems = 0
        for (w in wordsPlaced) {
            numElems++
            val word = translationMap[wordList[w]]
            val str = SpannableString(word)

            val typedValue: TypedValue = TypedValue()
            theme.resolveAttribute(R.attr.colorHighlight, typedValue, true)


            val color = if (foundWords.contains(word))
                typedValue.data
            else
                Color.rgb(255, 255, 255)
            str.setSpan(ForegroundColorSpan(color), 0, str.length, 0)
            bobTheBuilder.append(str)
            if (numElems != wordsPlaced.size)
                bobTheBuilder.append(", ")
        }
        wordsTextView.setText(bobTheBuilder, TextView.BufferType.SPANNABLE)
    }

    private fun markFoundWord(row: Int, col: Int, length: Int, direction: Int) {
        val typedValue: TypedValue = TypedValue()
        theme.resolveAttribute(R.attr.colorHighlight, typedValue, true)
        when (direction) {
            0 -> {  // left to right
                for (i in 0 until length) {
                    val textViewCell = getTableLayoutCell(tableLayout, row, col + i) as TextView
                    textViewCell.setTextColor(typedValue.data)
                }
            }
            1 -> {  // top down
                for (i in 0 until length) {
                    val textViewCell = getTableLayoutCell(tableLayout, row + i, col) as TextView
                    textViewCell.setTextColor(typedValue.data)
                }
            }
            2 -> {  // diagonally down
                for (i in 0 until length) {
                    val textViewCell = getTableLayoutCell(tableLayout, row + i, col + i) as TextView
                    textViewCell.setTextColor(typedValue.data)
                }
            }
        }
    }

    private fun buildGrid() {
        val orientation = resources.configuration.orientation
        val portrait = orientation == Configuration.ORIENTATION_PORTRAIT
        val margin = if (portrait) 2 else 0
        val tableParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.MATCH_PARENT
        )
        val rowParams = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(0, margin, 0, margin)
        }

        tableLayout = TableLayout(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            isStretchAllColumns = true
        }
        for (i in 0 until SIZE) {
            val tableRow = TableRow(this).apply {
                layoutParams = tableParams // TableLayout is the parent view
            }
            for (j in 0 until SIZE) {
                val textView = TextView(this).apply {
                    layoutParams = rowParams // TableRow is the parent view
                    gravity = Gravity.CENTER_HORIZONTAL
                    textSize = if (portrait) 25F else 17F
                }
                tableRow.addView(textView)
            }
            tableLayout.addView(tableRow)
        }
        val relativeLayout = findViewById<RelativeLayout>(R.id.relative_view)
        relativeLayout.addView(tableLayout)

        surfaceView = DrawingView(this).apply {
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
        }
        surfaceView.setParent(this)
        relativeLayout.addView(surfaceView)
    }

    private fun printGrid() {
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                val textViewCell = getTableLayoutCell(tableLayout, i, j) as TextView
                textViewCell.text = lettersGrid[i][j]
            }
        }
    }

    private fun getTableLayoutCell(layout: TableLayout, rowNo: Int, columnNo: Int): View? {
        if (rowNo >= layout.childCount)
            return null
        val row = layout.getChildAt(rowNo) as TableRow
        return if (columnNo >= row.childCount) null else row.getChildAt(columnNo)
    }

    private fun addRandomLetters() {
        for (i in 0 until SIZE) {
            for (j in 0 until SIZE) {
                if (lettersGrid[i][j] == null) {
                    lettersGrid[i][j] =
                        ('A'..'Z').random().toString() // TODO replace with german charset
                }
            }
        }
    }

    private fun createWordSearch(words: Array<String>, size: Int): List<Int> {
        val wordsPlaced = mutableListOf<Int>()
        words.sortByDescending { it.length }
        lettersGrid = Array(size) { arrayOfNulls<String>(size) }
        detectionGrid = Array(size) { arrayOfNulls<Int>(size) }
        for (wordIndex in words.indices) {
            val word = words[wordIndex]
            val wordLength = word.length
            var attempts = 20

            val direction = (0..2).random()
            loop@ while (attempts-- > 0) {  // cool 'approaching zero' operator
                when (direction) { // random direction
                    0 -> {  // left to right
                        val row = (0 until size).random()
                        val col = (0 until size - wordLength).random()
                        for (i in word.indices) {
                            if (lettersGrid[row][col + i] != null) {
                                continue@loop
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 0)
                        wordsPlaced.add(wordIndex)
                        break@loop
                    }
                    1 -> {  // top down
                        val row = (0 until size - wordLength).random()
                        val col = (0 until size).random()
                        for (i in word.indices) {
                            if (lettersGrid[row + i][col] != null) {
                                continue@loop
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 1)
                        wordsPlaced.add(wordIndex)
                        break@loop
                    }
                    2 -> {  // diagonally down
                        val row = (0 until size - wordLength).random()
                        val col = (0 until size - wordLength).random()
                        for (i in word.indices) {
                            if (lettersGrid[row + i][col + i] != null) {
                                continue@loop
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 2)
                        wordsPlaced.add(wordIndex)
                        break@loop
                    }
                }
            }
        }

        return wordsPlaced.toList()  // return indicator of which words were successfully placed
    }

    private fun writeWord(word: String, wordIndex: Int, row: Int, col: Int, direction: Int) {
        when (direction) {
            0 -> {  // left to right
                detectionGrid[row][col] = wordIndex  // mark beginning and ending point of word
                detectionGrid[row][col + word.length - 1] = wordIndex
                for (i in word.indices) {
                    lettersGrid[row][col + i] = word[i].toString()
                }
            }
            1 -> {  // top down
                detectionGrid[row][col] = wordIndex
                detectionGrid[row + word.length - 1][col] = wordIndex
                for (i in word.indices) {
                    lettersGrid[row + i][col] = word[i].toString()

                }
            }
            2 -> {  // diagonally down
                detectionGrid[row][col] = wordIndex
                detectionGrid[row + word.length - 1][col + word.length - 1] = wordIndex
                for (i in word.indices) {
                    lettersGrid[row + i][col + i] = word[i].toString()
                }
            }

        }

    }
}