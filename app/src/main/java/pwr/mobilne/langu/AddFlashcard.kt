package pwr.mobilne.langu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import java.security.AccessController.getContext


class AddFlashcard : AppCompatActivity() {
    lateinit var radioGroup : RadioGroup
    lateinit var flashcardDescription : EditText
    lateinit var flashcardName : EditText
    lateinit var flashcardNotes : EditText
    lateinit var category : EditText
    lateinit var selectedCategory : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)
        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        flashcardDescription = findViewById(R.id.edit_flashcard_description)
        flashcardName= findViewById(R.id.edit_flashcard_name)
        flashcardNotes = findViewById(R.id.edit_flashcard_notes)
        category = findViewById(R.id.category)
        selectedCategory = ""
        val categories = intent.getStringArrayExtra("categories")
        //radioGroup.add(categories)

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

        /*radioGroup.setOnCheckedChangeListener { group, checkedId -> // find which radio button is selected
            if (checkedId == R.id.radioNoun) {
                Toast.makeText(
                    applicationContext, "choice: Silent",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (checkedId == R.id.radioVerb) {
                Toast.makeText(
                    applicationContext, "choice: Sound",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (checkedId == R.id.radioOther) {
                Toast.makeText(
                    applicationContext, "choice: Vibration",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }*/
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