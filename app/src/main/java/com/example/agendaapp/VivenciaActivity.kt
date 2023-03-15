package com.example.agendaapp

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.agendaapp.databinding.ActivityVivenciaBinding
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class VivenciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVivenciaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVivenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras
        val index: Int = bundle?.get("VIVENCIA") as Int

        val nota = File(filesDir.absolutePath+"/notas/").listFiles()
        val name = nota[index].name
        binding.tvTitulo.text = name
        readFile(nota[index].absolutePath)

        val image = File(filesDir.absolutePath+"/images/").listFiles()
        val size = image.size-1

        for(i in 0..size){

            val nameNota = nota[index].name.split(".")

            val nameImage = image[i].name.split(".")


            if( nameImage[0] == nameNota[0]){
                val imgBitmap = BitmapFactory.decodeFile(image[i].absolutePath)

                binding.imgVivencia.setImageBitmap(imgBitmap)
            }
        }

        binding.btnPlay.setOnClickListener {
        val nameNota = nota[index].name.split(".")
            playAudio(nameNota[0])
        }
    }

    private fun readFile(path:String){

        var file = File(path)

        var fileInputStream = FileInputStream(file)
        var inputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader = BufferedReader(inputStreamReader)

        var text: String? = null
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            if (text?.startsWith("Fecha") == true){
                binding.tvFecha.text = text
            }
            else{
                binding.tvDescripcion.text = text
            }

        }
        fileInputStream.close()
    }

    private fun playAudio(name: String){
        val audio = File(filesDir.absolutePath+"/audios/").listFiles()
        val sizeAudio = audio.size-1

        for(i in 0..sizeAudio){

            val nameAudio = audio[i].name.split(".")

            if( nameAudio[0] == name){
                var mp = MediaPlayer()
                mp.setDataSource(audio[i].absolutePath)
                mp.prepare()
                mp.start()
            }

            else{
                Toast.makeText(this, "Esta vivencia no tiene audio", Toast.LENGTH_SHORT).show()
            }
        }
    }
}