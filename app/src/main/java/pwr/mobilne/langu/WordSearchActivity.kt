package pwr.mobilne.langu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.StringBuilder

const val SIZE = 10

class WordSearchActivity : AppCompatActivity() {

    private lateinit var grid: Array<Array<Char?>>
    private lateinit var detectionGrid: Array<Array<Int?>>
    //TODO("make UI")

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_search)
        val wordList = arrayOf(
            "hello",
            "world",
            "awds",
            "bibgfg",
            "CCC",
            "aaaaaaaa",
            "bbbbbbbb"
        )
        //val wordList = intent.getStringArrayListExtra("wordList")
        wordList.filter { it.length < SIZE }
        val wordsPlaced = createWordSearch(wordList, SIZE)
        addRandomLetters()
        printGrid()
        printWords(wordList, wordsPlaced)
    }
    fun printGrid(){
        val bobTheBuilder = StringBuilder()
        for( i in 0 until SIZE){
            for (j in 0 until SIZE){
                bobTheBuilder.append("${grid[i][j]}, ")
            }
            bobTheBuilder.append("\n")
        }
        println(bobTheBuilder.toString())
    }
    fun printWords(words: Array<String>, wordsPlaced: List<Int>){
        println("words placed")
        for (w in wordsPlaced){
            println(words[w])
        }
    }

    fun addRandomLetters(){
        for( i in 0 until SIZE){
            for (j in 0 until SIZE){
                if(grid[i][j] == null){
                    grid[i][j] = '-' //('A'..'Z').random()
                }
            }
        }
    }

    private fun createWordSearch(words: Array<String>, size: Int): List<Int> {
        val wordsPlaced = mutableListOf<Int>()
        words.sortByDescending { it.length }
        grid = Array(size) { arrayOfNulls<Char>(size) }
        detectionGrid = Array(size) { arrayOfNulls<Int>(size) }
        for (wordIndex in words.indices) {
            val word = words[wordIndex]
            val wordLength = word.length
            var attempts = 10
            inner@ while (attempts --> 0) {  // cool 'approaching' operator -->
                when ((0..3).random()) {
                    0 -> {  // left to right
                        val row = (0 until size).random()
                        val col = (0 until size - wordLength).random()
                        for (i in word.indices) {
                            if (detectionGrid[row][col + i] != null) {
                                continue@inner
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 0)
                        wordsPlaced.add(wordIndex)
                        break@inner
                    }
                    1 -> {  // top down
                        val row = (0 until size - wordLength).random()
                        val col = (0 until size).random()
                        for (i in word.indices) {
                            if (detectionGrid[row + i][col] != null) {
                                continue@inner
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 1)
                        wordsPlaced.add(wordIndex)
                        break@inner
                    }
                    2 -> {  // diagonally down
                        val row = (0 until size - wordLength).random()
                        val col = (0 until size - wordLength).random()
                        for (i in word.indices) {
                            if (detectionGrid[row + i][col + i] != null) {
                                continue@inner
                            }
                        }
                        writeWord(word, wordIndex, row, col, direction = 2)
                        wordsPlaced.add(wordIndex)
                        break@inner
                    }

                }
            }
        }
        return wordsPlaced.toList()
    }

    private fun writeWord(word: String, wordIndex: Int, row: Int, col: Int, direction: Int){
        when(direction){
            0 -> {  // left to right
                for (i in word.indices) {
                    grid[row][col + i] = word[i]
                    detectionGrid[row][col + i] = wordIndex
                }
            }
            1 -> {  // top down
                for (i in word.indices) {
                    grid[row + i][col] = word[i]
                    detectionGrid[row + i][col] = wordIndex

                }
            }
            2 -> {  // diagonally down
                for (i in word.indices) {
                    grid[row + i][col + i] = word[i]
                    detectionGrid[row + i][col + i] = wordIndex
                }
            }

        }

    }
}