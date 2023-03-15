package com.example.agendaapp

import CustomAdapter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agendaapp.databinding.ActivityListVivenciasBinding
import java.io.File


class ListVivenciasActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListVivenciasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListVivenciasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegresar.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnEliminarTodo.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Eliminar todas las vivencias")
                .setMessage("¿Estas seguro de querer eliminar todo?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, which ->
                    val nota = File(filesDir.absolutePath+"/notas/")
                    val listaNota = nota.listFiles()
                    val notaSize = listaNota.size - 1
                    for(i in 0..notaSize){
                        val archivo = File(filesDir.absolutePath+"/notas/"+listaNota[i].name)
                        archivo.delete()
                    }

                    val image = File(filesDir.absolutePath+"/images/")
                    val listaImage = image.listFiles()
                    val imageSize = listaImage.size - 1
                    for(i in 0..imageSize){
                        val archivo = File(filesDir.absolutePath+"/images/"+listaImage[i].name)
                        archivo.delete()
                    }

                    val audio = File(filesDir.absolutePath+"/audios/")
                    val listaAudio = audio.listFiles()
                    val audioSize = listaAudio.size - 1
                    for(i in 0..audioSize){
                        val archivo = File(filesDir.absolutePath+"/audios/"+listaAudio[i].name)
                        archivo.delete()
                    }
                    Toast.makeText(this, "¡Todas sus vivencias han sido eliminadas con exito!", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No") { dialog, which ->
                    Toast.makeText(this, "Tus notas no han sido borradas", Toast.LENGTH_SHORT).show()
                }

            val dialog = builder.create()
            dialog.show()
        }

        val recyclerview = binding.recyclerview

        recyclerview.layoutManager = LinearLayoutManager(this)

        val data = ArrayList<ItemsViewModel>()
        val size = File(filesDir.absolutePath+"/notas/").listFiles().size - 1

        for (i in 0..size) {
            try{
                val nota = File(filesDir.absolutePath+"/notas/")
                val listaNota = nota.listFiles()
                if(listaNota.isNotEmpty()) {

                    val name = listaNota[i].name.split(".")

                    data.add(ItemsViewModel(name[0]))
                }

            }catch (e:OutOfMemoryError) {
                e.printStackTrace()
            }
        }
        val adapter = CustomAdapter(data)

        recyclerview.adapter = adapter
    }
}