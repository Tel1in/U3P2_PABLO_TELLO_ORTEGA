package mx.tecnm.tepic.u3p2_pablo_tello_ortega.ui.gallery

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.databinding.FragmentGalleryBinding
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.editarPro

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    var listaID = ArrayList<String>()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val baseDatos = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrar()

        binding.btnInser.setOnClickListener {
            val baseDatos = FirebaseFirestore.getInstance()
            val datos = hashMapOf(
                "CURP" to binding.curp.text.toString(),
                "NOMBRE" to binding.nombre.text.toString(),
                "TELEFONO" to binding.tel.text.toString(),
                "EDAD" to binding.edad.text.toString().toInt()

            )
            baseDatos.collection("Propietario")
                .add(datos)
                .addOnSuccessListener {
                    //SISEPUDO
                    Toast.makeText(requireContext(),"SI SE INSERTO CON EXITO",Toast.LENGTH_LONG)
                        .show()
                    binding.curp.text.clear()
                    binding.nombre.text.clear()
                    binding.tel.text.clear()
                    binding.edad.text.clear()
                    mostrar()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()


                }


        }

        binding.btnAll.setOnClickListener {
            mostrarTodos()
        }

        binding.btnCel.setOnClickListener {
            mostrarPorTelefono()
        }

        binding.btnMenor.setOnClickListener {
            mostrarPorEdadMenos()
        }

        binding.btnMayor.setOnClickListener {
            mostrarPorEdadMas()
        }
        return root
    }

    fun mostrar(){
        FirebaseFirestore.getInstance()
            .collection("Propietario")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                for (documento in query!!){
                    var cadena = "Curp: ${documento.getString("CURP")}\n"+
                            "Nombre: ${documento.getString("NOMBRE")}"
                    arreglo.add(cadena)
                    listaID.add(documento.id)
                }

                binding.lista.adapter=ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)

                binding.lista.setOnItemClickListener { adapterView, view, i, l ->
                    var idSeleccionado = listaID.get(i)

                    AlertDialog.Builder(requireContext())
                        .setTitle("ATENCION")
                        .setMessage("¿Que deseas realizar con el ID selecionado ?")
                        .setNegativeButton("ELIMINAR"){d,i->
                            eliminar(idSeleccionado)
                        }
                        .setPositiveButton("ACTUALIZAR"){d,i->
                            actualizar(idSeleccionado)
                        }
                        .setNeutralButton("SALIR"){d,i->
                        }
                        .show()
                }
            }
    }
    fun eliminar(idSeleccionado: String){
        val baseDatos = FirebaseFirestore.getInstance()
        baseDatos.collection("Propietario")
            .document(idSeleccionado)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"SI SE ELIMINO CON EXITO",Toast.LENGTH_LONG)
                .show()
                mostrar()
            }
            .addOnFailureListener {
                AlertDialog.Builder(requireContext())
                .setMessage(it.message)
                .show()
            }
    }

    fun actualizar(idSeleccionado: String){
        var otraVentana = Intent(requireContext(),editarPro::class.java)
        otraVentana.putExtra("idSeleccionado",idSeleccionado)
        startActivity(otraVentana)
    }

    fun mostrarTodos(){
        FirebaseFirestore.getInstance()
            .collection("Propietario")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                for (documento in query!!){
                    val cad = "Curp: ${documento.getString("CURP")}"+ "  " +
                            "Nombre: ${documento.getString("NOMBRE")}"+"   "+
                            "Telefono: ${documento.getString("TELEFONO")}"+"   "+
                            "Edad: ${documento.getLong("EDAD")}"
                    arreglo.add(cad)
                }

                binding.lista.adapter=ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)
            }
    }

    fun mostrarPorTelefono(){
        FirebaseFirestore.getInstance()
            .collection("Propietario")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                val telefonoElejido = binding.tel.text.toString()
                for (documento in query!!) {
                    if (documento.getString("TELEFONO") == telefonoElejido) {
                        val cad = "Curp: ${documento.getString("CURP")}" + "  " +
                                "Telefono: ${documento.getString("TELEFONO")}"
                        arreglo.add(cad)
                    }

                }

                binding.lista.adapter=ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)
            }
    }
    fun mostrarPorEdadMenos(){
        FirebaseFirestore.getInstance()
            .collection("Propietario")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                val edad = binding.edad.text.toString().toInt()
                for (documento in query!!) {
                    if (documento.getLong("EDAD")!! <= edad) {
                        val cad = "Curp: ${documento.getString("CURP")}" + "  " +
                                "Edad: ${documento.getLong("EDAD")}"
                        arreglo.add(cad)
                    }

                }

                binding.lista.adapter=ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)
            }
    }

    fun mostrarPorEdadMas(){
        FirebaseFirestore.getInstance()
            .collection("Propietario")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                val edad = binding.edad.text.toString().toInt()
                for (documento in query!!) {
                    if (documento.getLong("EDAD")!! >= edad) {
                        val cad = "Curp: ${documento.getString("CURP")}" + "  " +
                                "Edad: ${documento.getLong("EDAD")}"
                        arreglo.add(cad)
                    }

                }

                binding.lista.adapter=ArrayAdapter<String>(requireContext(),
                    android.R.layout.simple_list_item_1,arreglo)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}