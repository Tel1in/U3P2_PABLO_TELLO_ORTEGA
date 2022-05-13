package mx.tecnm.tepic.u3p2_pablo_tello_ortega

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.databinding.ActivityEditarProBinding

class editarPro : AppCompatActivity() {
    var idSeleccionado = " "
    lateinit var binding: ActivityEditarProBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarProBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idSeleccionado = intent.extras!!.getString("idSeleccionado")!!

        val baseDatos = FirebaseFirestore.getInstance()
        baseDatos.collection("Propietario")
            .document(idSeleccionado)
            .get()
            .addOnSuccessListener {
                binding.nombre.setText(it.getString("NOMBRE"))
                binding.tel.setText(it.getString("TELEFONO"))
                binding.edad.setText(it.getLong("EDAD").toString())
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
            baseDatos.collection("Propietario")
                .document(idSeleccionado)
                .update("NOMBRE" , binding.nombre.text.toString()
                    ,"TELEFONO" , binding.tel.text.toString()
                    ,"EDAD" , binding.edad.text.toString().toInt())
                .addOnSuccessListener {
                    //SISEPUDO
                    Toast.makeText(this,"SI SE ACTUALIZO CON EXITO",Toast.LENGTH_LONG)
                        .show()
                    binding.nombre.text.clear()
                    binding.tel.text.clear()
                    binding.edad.text.clear()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(this)
                        .setMessage(it.message)
                        .show()
                }
        }
    }
}