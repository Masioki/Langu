package pwr.mobilne.langu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AddFlashcard : AppCompatActivity() {
    lateinit var radioGroup : RadioGroup
    lateinit var flashcardDescription : EditText
    lateinit var flashcardName : EditText
    lateinit var flashcardNotes : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_flashcard)
        radioGroup = findViewById<View>(R.id.radioGroup) as RadioGroup
        flashcardDescription = findViewById(R.id.edit_flashcard_description)
        flashcardName= findViewById(R.id.edit_flashcard_name)
        flashcardNotes = findViewById(R.id.edit_flashcard_notes)
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
        //TODO: implement adding to db
    }

    private fun checkFilled() : Boolean {
        var errors = ""
        if(radioGroup.checkedRadioButtonId  == -1){
            errors += "Please, check appropriate part of speech. "
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
}