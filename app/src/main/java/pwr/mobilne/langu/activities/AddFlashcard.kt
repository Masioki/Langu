package pwr.mobilne.langu.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import pwr.mobilne.langu.R
import java.util.*


class AddFlashcard : AppCompatActivity() {
    lateinit var radioGroup : RadioGroup
    lateinit var flashcardDescription : EditText
    lateinit var flashcardName : EditText
    lateinit var flashcardNotes : EditText
    lateinit var category : EditText
    lateinit var selectedCategory : String
    lateinit var laguageSelected : Locale
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)
//        findViewById<FloatingActionButton>(R.id.fab).hide()
//        findViewById<FloatingActionButton>(R.id.fab).show()
//        findViewById<FloatingActionButton>(R.id.fab).setImageResource(R.drawable.save_icon)

        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        flashcardDescription = findViewById(R.id.edit_flashcard_description)
        flashcardName= findViewById(R.id.edit_flashcard_name)
        flashcardNotes = findViewById(R.id.edit_flashcard_notes)
        category = findViewById(R.id.category)
        selectedCategory = ""
        val categories = intent.getStringArrayExtra("categories")
        categories?.forEach { passedCategory ->
            val rb = RadioButton(   this)
            rb.text = passedCategory
            rb.id = View.generateViewId()
            rb.layoutParams = ViewGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, 50f.toDips().toInt())
            rb.gravity = Gravity.CENTER_HORIZONTAL
            rb.textSize = 18f
            radioGroup.addView(rb)
        }
        val fab: View = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            createFlashcard()
        }

        /***
         * spinner
         */
        val spinner: Spinner = findViewById(R.id.planets_spinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> laguageSelected = Locale.ENGLISH
                    1 -> laguageSelected = Locale.FRENCH
                    2 -> laguageSelected = Locale.CHINESE
                    3 -> laguageSelected = Locale.ITALIAN
                    4 -> laguageSelected = Locale.JAPANESE
                    5 -> laguageSelected = Locale.KOREAN
                    else -> { // Note the block
                        laguageSelected = Locale.ENGLISH
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                laguageSelected = Locale.ENGLISH
            }
        }


    }

    fun createFlashcard(){
        checkFilled()
        appendToVocabulary()
    }

    private fun appendToVocabulary(){
        val myIntent = Intent()
        Log.println(Log.ERROR, "am", "Out")
        myIntent.putExtra("native", flashcardDescription.text.toString())
        myIntent.putExtra("german", flashcardName.text.toString())
        myIntent.putExtra("language", laguageSelected)
        myIntent.putExtra("category", selectedCategory)
        setResult(Activity.RESULT_OK, myIntent)
        finish()
    }

    private fun checkFilled() : Boolean {
        var errors = ""
        if(category.text.toString() == "") {
            if (radioGroup.checkedRadioButtonId == -1) {
                errors += "Please, check appropriate category. "
            }
            else{
                selectedCategory = findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
            }
        }
        else{
            selectedCategory = category.text.toString()
            Log.println(Log.WARN, "zksfjg", "CATEGORY NOT EMPTY : $selectedCategory")
        }
        if(flashcardName.text.isEmpty()){
            errors += "Please, input flashcard name. "
        }
        if(flashcardDescription.text.isEmpty()){
            errors += "Please, input flashcard translation. "
        }
        if(errors.equals("")){
            return true
        }
        else{
            Toast.makeText(
                applicationContext, errors,
                Toast.LENGTH_LONG
            ).show()
            return false
        }
    }
    fun Float.toDips() =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics);
}