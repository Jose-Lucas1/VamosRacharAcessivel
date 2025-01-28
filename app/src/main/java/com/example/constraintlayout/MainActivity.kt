package com.example.constraintlayout

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import java.util.*
import kotlin.math.ceil

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtNDePessoas: EditText
    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.valorDaConta)
        edtNDePessoas = findViewById<EditText>(R.id.numDePessoas)
        edtConta.addTextChangedListener(this)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mudancaNoTexto()
            // This method is called before the text is changed.
                //println("Before Text Changed: $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                mudancaNoTexto()
            // This method is called when the text is being changed.
                //println("On Text Changed: $s")
            }

            override fun afterTextChanged(s: Editable?) {
                mudancaNoTexto()
                // This method is called after the text has been changed.
                //println("After Text Changed: ${s.toString()}")
            }
        }


        edtConta.addTextChangedListener(textWatcher)
        edtNDePessoas.addTextChangedListener(textWatcher)

        //val bt_falar: Button =findViewById(R.id.btFalar)
        /*
        bt_falar.setOnLongClickListener{
            //Toast.makeText(this, "Long click detected", Toast.LENGTH_SHORT).show()
            //return@setOnLongClickListener
            clickShare(bt_falar)
            true
        }
        */
    }

    fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        val valor: Double

        if(s.toString().length>0) {
             valor = s.toString().toDouble()
            Log.d("PDM24", "v: " + valor)
        //    edtConta.setText("9")
        }
    }

    fun clickFalar(v: View){
        val fieldValorDistribuido: TextView = findViewById (R.id.valorDistribuido)
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak(fieldValorDistribuido.text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
        /*
        val intent = Intent(this,Activity2::class.java).apply {
            putExtra("nome","asçdlfkjasdlçfiasjdfljkçaslçdfijasdlçfkjaaslçfjaslçfjasldfjalçsdfjasklçfjlçasdkfjaslçdkfjasdlçfjk")
            putExtra("valor2",40)
        }
        startActivity(intent)
         */



    }

    fun mudancaNoTexto() {
        val fieldValorDaConta: EditText = findViewById (R.id.valorDaConta)
        val fieldNumeroDePessoas: EditText = findViewById (R.id.numDePessoas)
        val fieldValorDistribuido: TextView = findViewById (R.id.valorDistribuido)
        val valor = fieldValorDaConta.text
        if (isNumeric(valor.toString()) && isNumeric(fieldNumeroDePessoas.text.toString()) ) {
            val valorDistribuido = valor.toString().toDouble()/fieldNumeroDePessoas.text.toString().toDouble()
            fieldValorDistribuido.text = "R$ ${String.format(Locale.ENGLISH, "%.2f", valorDistribuido).toDouble()}"
        }
    }

    fun clickComprar(v: View){
        val fieldValorDaConta: EditText = findViewById (R.id.valorDaConta)
        val fieldNumeroDePessoas: EditText = findViewById (R.id.numDePessoas)
        val fieldValorDistribuido: TextView = findViewById (R.id.valorDistribuido)
        val valor = fieldValorDaConta.text
        if (isNumeric(valor.toString()) && isNumeric(fieldNumeroDePessoas.text.toString()) ) {
            val valorDistribuido = valor.toString().toDouble()/fieldNumeroDePessoas.text.toString().toDouble()
            fieldValorDistribuido.text = "R$ ${String.format(Locale.ENGLISH, "%.2f", valorDistribuido).toDouble()}"
        }
    }
    /*
    fun clickComprar(v: View){
        val it_Youtube = Intent(Intent.ACTION_VIEW)
        it_Youtube.data= Uri.parse("https://www.youtube.com/")
        startActivity(it_Youtube);
    }
     */

    fun clickShare(v: View){
        val fieldValorDistribuido: TextView = findViewById (R.id.valorDistribuido)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, fieldValorDistribuido.text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

