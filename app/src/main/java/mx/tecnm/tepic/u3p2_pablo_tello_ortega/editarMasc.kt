package mx.tecnm.tepic.u3p2_pablo_tello_ortega

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.databinding.ActivityEditarMascBinding
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.databinding.ActivityEditarProBinding

class editarMasc : AppCompatActivity() {
    var idSeleccionado = " "
    lateinit var binding: ActivityEditarMascBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarMascBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idSeleccionado")!!

        val baseDatos = FirebaseFirestore.getInstance()
        baseDatos.collection("Mascota")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.nombreM.setText(it.getString("NOMBRE"))
                binding.raza.setText(it.getString("RAZA"))
                Toast.makeText(this,"SI SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                    .show()
            }
            .addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage(it.message)
                    .show()
            }

        binding.btnRegresar.setOnClickListener {
            finish()
        }

        binding.btnUpdate.setOnClickListener {
            baseDatos.collection("Mascota")
                .document(idSeleccionado)
                .update("NOMBRE" , binding.nombreM.text.toString()
                    ,"RAZA" , binding.raza.text.toString())
                .addOnSuccessListener {
                    //SISEPUDO
                    Toast.makeText(this,"SI SE ACTUALIZO CON EXITO", Toast.LENGTH_LONG)
                        .show()
                    binding.nombreM.text.clear()
                    binding.raza.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}