package pwr.mobilne.langu.activities

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import pwr.mobilne.langu.R
import pwr.mobilne.langu.activities.game.hangman.HangmanActivity
import pwr.mobilne.langu.activities.game.wordsearch.WordSearchActivity
import pwr.mobilne.langu.activities.game.wordsearch.WordsearchConst
import pwr.mobilne.langu.data.WordEntity
import pwr.mobilne.langu.data.WordViewModel
import pwr.mobilne.langu.databinding.ActivityMainBinding
import pwr.mobilne.langu.reminders.NotificationScheduleWorker
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var wordsLista: MutableList<WordEntity>
    private lateinit var categories: MutableList<String>
    private lateinit var uvm: WordViewModel

    companion object {
        lateinit var appLanguage: String
    }

    /**
     * onCreate - przy tworzeniu
     */
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val category = intent?.getStringExtra("category")
                val german = intent?.getStringExtra("german")
                val native = intent?.getStringExtra("native")
                val language = intent?.getSerializableExtra("language")
                if (german != null && native != null && category != null) {
                    val word = WordEntity(0, german, native, language as Locale, category)
                    uvm.addWord(word)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        uvm = ViewModelProvider(this).get(WordViewModel::class.java)
        wordsLista =
            listOf(WordEntity(0, "a1a", "13", Locale.ENGLISH, "arh")) as MutableList<WordEntity>

        appLanguage =
            this.getPreferences(Context.MODE_PRIVATE).getString("langu_language", "fr").toString()
        val view = binding.root
        setContentView(view)


        /**
         * Selectowanie z bazy danych
         */
        uvm.readAllData.observe(this, Observer { status ->
            this.wordsLista = status as MutableList<WordEntity>
            val langs = wordsLista.map { w -> w.laguage.toString() }.distinct()
            val index = langs.indexOf(appLanguage)
            val spinner =
                (findViewById<Toolbar>(R.id.toolbar)).findViewById<Spinner>(R.id.toolbar_spinner)
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, langs)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.setSelection(index)
//            spinner.onItemSelectedListener = AdapterView.OnItemSelectedListener{
            spinner.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    setLanguage(langs[position])
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    setLanguage("de")
                }
            }
            setSupportActionBar(findViewById(R.id.toolbar))
        })
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })

        binding.button.setOnClickListener {
            startWordsearch()
        }

        val btnAdd: TextView? = findViewById(R.id.buttonAdd)
        val btnSearch: TextView? = findViewById(R.id.button)
        val btnHangman: TextView? = findViewById(R.id.buttonHangman)
        val moveUp = TranslateAnimation(0F, 0F, 400F, 0F)
        moveUp.setDuration(1000)
        moveUp.setFillAfter(true)
        btnAdd?.startAnimation(moveUp)
        btnSearch?.startAnimation(moveUp)
        btnHangman?.startAnimation(moveUp)
        setNotifications()
    }

    fun setLanguage(l: String) {
        appLanguage = l
        with(this.getPreferences(MODE_PRIVATE).edit()){
            putString("langu_language", appLanguage)
            apply()
        }
        //this.getPreferences(Context.MODE_PRIVATE).edit().putString("language", appLanguage).apply()
    }

    private fun setNotifications() {
        createNotificationChannel()
        for (i in 0..6) {
            val date = GregorianCalendar()
            date.set(Calendar.HOUR_OF_DAY, 16)
            date.set(Calendar.MINUTE, 30)
            date.set(Calendar.SECOND, 0)
            date.set(Calendar.MILLISECOND, 0)
            date.add(Calendar.DAY_OF_MONTH, i)

            val tag = date.toString()
            val now = Date()

            WorkManager.getInstance(application).cancelAllWorkByTag(tag)
            val delayInMinutes = (date.time.time - now.time) / 60000
            val notificationWork =
                OneTimeWorkRequest.Builder(NotificationScheduleWorker::class.java)
                    .setInitialDelay(delayInMinutes, TimeUnit.MINUTES)
                    .addTag(tag)
                    .build()
            WorkManager.getInstance(application).enqueue(notificationWork)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            0.toString(),
            "Langu notification",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Langu notification"
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun startWordsearch() {
        // direction:
        // 0: nativs on board, Deutsch displayed
        // 1: Deutsch on board, nativs displayed
        val translationDirection = (0..1).random()

        val words = wordsLista.toTypedArray()
        val language = words[0].laguage
        // shuffle the list ant take a number of first elements
        words.shuffle()
        val list = words
            .filter { w -> w.laguage == Locale.forLanguageTag(appLanguage) }
            .take(WordsearchConst.SIZE)
            .toTypedArray()
        val map = hashMapOf<String, String>()

        when (translationDirection) {
            0 -> {
                for (word in list) {
                    map[word.nativs.uppercase()] = word.german.uppercase()
                }
            }
            1 -> {
                for (word in list) {
                    map[word.german.uppercase()] = word.nativs.uppercase()
                }
            }
        }

        val int = Intent(this, WordSearchActivity::class.java)
        int.putExtra("translationMap", map)
        int.putExtra("language", language)
        startActivity(int)
    }

    fun addFlashcard(view: View) {
        val intent = Intent(this, AddFlashcard::class.java).apply {
        }
        uvm.getAllCategories.observe(this, Observer { status ->
            this.categories = status as MutableList<String>
        })
        var catArray: Array<String?> = arrayOfNulls(this.categories.size)
        for (i in 0 until this.categories.size) {
            catArray[i] = this.categories[i]
        }
        intent.putExtra("categories", catArray)
        //startActivityForResult(intent, 111)
        startForResult.launch(intent)

    }

    fun playHangman(view: View) {
        val intent = Intent(this, HangmanActivity::class.java)
        val word = WordEntity(0, "Juni", "June", Locale.GERMAN, "calendar")
        intent.putExtra("word", word)
        startActivity(intent)
    }

}