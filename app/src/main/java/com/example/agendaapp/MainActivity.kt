package com.example.agendaapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.agendaapp.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private var mr: MediaRecorder = MediaRecorder()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dirNota = File(filesDir.absolutePath + "/notas/")
        val dirImages = File(filesDir.absolutePath + "/images/")
        val dirAudios = File(filesDir.absolutePath + "/audios/")

        if(!dirNota.exists() && !dirImages.exists() && !dirAudios.exists()){
            dirNota.mkdir()
            dirImages.mkdir()
            dirAudios.mkdir()
        }

        val intent = Intent(this, ListVivenciasActivity::class.java)

        //Evento del boton Lista para desplegar una lista con todas las vivencias
        binding.btnlista.setOnClickListener {
            startActivity(intent)
        }

        //Creacion de una variable photo para guardar la foto capturada por camara
        val photo = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){
            if (it != null) {
                takeImage(binding.etTitulo.text.toString(), it)
            }
        }

        //Evento click del boton camera para tomar una foto con la camara del telefono
        binding.btnCamera.setOnClickListener {
        if (binding.etTitulo.text.toString().isNotEmpty()){
            photo.launch()
            binding.tvCamera.text = binding.etTitulo.text.toString()
        }
            else{
                Toast.makeText(this, "Introduzca un titulo para la vivencia", Toast.LENGTH_SHORT).show()
            }
        }

        //Evento click del boton gallery para buscar imagenes en la galeria
        binding.btnGallery.setOnClickListener {
            if (binding.etTitulo.text.toString().isNotEmpty()){
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery,100)
                binding.tvCamera.text = binding.etTitulo.text.toString()
            }
            else{
                Toast.makeText(this, "Introduzca un titulo para la vivencia", Toast.LENGTH_SHORT).show()
            }
        }

        //Permisos para el Microfono
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO),111)

        //Evento click para el boton de microfono y grabar audio
        binding.btnMic.setOnClickListener {
            if (binding.etTitulo.text.toString().isNotEmpty()){
                takeAudio(binding.etTitulo.text.toString())
            }
            else{
                Toast.makeText(this, "Introduzca un titulo para la vivencia", Toast.LENGTH_SHORT).show()
            }

        }

        //Evento click del boton guardar para que se registren las vivencias en el local storage
        binding.btnGuardar.setOnClickListener {
            if (binding.etTitulo.text.toString().isNotEmpty()) {
                writeFile(
                    binding.etTitulo.text.toString(),
                    binding.etDescripcion.text.toString(),
                    binding.etFecha.text.toString()
                )
                Toast.makeText(this, "Su vivencia ha sido registrada con exito", Toast.LENGTH_SHORT).show()
                binding.etTitulo.text.clear()
                binding.etFecha.text?.clear()
                binding.etDescripcion.text.clear()
                binding.tvMic.text ="Aquí va su audio..."
                binding.tvCamera.text="Aquí va su foto..."
            }
            else{
                Toast.makeText(this, "Introduzca un titulo para la vivencia", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Funcion usada para guardar un archivo txt con el titulo, fecha y descripcion de la vivencias
    private fun writeFile(Nombre: String, Descripcion: String, Fecha: String){
        val nombreFile:String = Nombre
        val data = "$Descripcion\nFecha: $Fecha"

        val path = File(filesDir.absolutePath+"/notas","$nombreFile.txt")
        val fileOutputStream = FileOutputStream(path)
        try {
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    //Funcion usada para tomar una imagen, sea de la galeria o de la camara, para guardarla en el local storage
    private fun takeImage(Nombre: String, bmp: Bitmap){
        val path = File(filesDir.absolutePath+"/images","$Nombre.jpg")
        val fileOutputStream = FileOutputStream(path)
        fileOutputStream.use{stream ->
            bmp.compress(Bitmap.CompressFormat.JPEG, 95,stream)
        }
    }

    //Funcion usada para tomar el audio grabado por el microfono guardarlo en el local storage
    @RequiresApi(Build.VERSION_CODES.O)
    private fun takeAudio(Nombre: String){
        val path = File(filesDir.absolutePath+"/audios","$Nombre.3gp")

        if(binding.tvMic.isEnabled){
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()
            binding.tvMic.isEnabled = false
            Toast.makeText(this, "Se esta grabando audio...", Toast.LENGTH_SHORT).show()
        }
        else{
            mr.stop()
            binding.tvMic.isEnabled = true
            binding.tvMic.text = binding.etTitulo.text.toString()
            Toast.makeText(this, "¡Su audio ha sido creado con exito!", Toast.LENGTH_SHORT).show()
        }
    }

    //Esta funcion es para tomar la foto de la galeria y guardarla en el local storage
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            val imageGallery =  data?.data
            val imgBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,imageGallery)
            takeImage(binding.etTitulo.text.toString(),imgBitmap)
        }
    }
}