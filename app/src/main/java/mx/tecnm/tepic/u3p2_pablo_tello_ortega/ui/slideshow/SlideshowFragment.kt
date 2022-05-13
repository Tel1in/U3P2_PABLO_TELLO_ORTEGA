package mx.tecnm.tepic.u3p2_pablo_tello_ortega.ui.slideshow

import android.R
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
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.databinding.FragmentSlideshowBinding
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.editarMasc
import mx.tecnm.tepic.u3p2_pablo_tello_ortega.editarPro

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var listaID = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mostrar()

        binding.btnInser.setOnClickListener {
            val baseDatos = FirebaseFirestore.getInstance()
            val datos = hashMapOf(
                "NOMBRE" to binding.nombreM.text.toString(),
                "RAZA" to binding.raza.text.toString(),
                "CURP" to binding.curp1.text.toString()

            )
            baseDatos.collection("Mascota")
                .add(datos)
                .addOnSuccessListener {
                    //SISEPUDO
                    Toast.makeText(requireContext(),"SI SE INSERTO CON EXITO", Toast.LENGTH_LONG)
                        .show()
                    binding.nombreM.text.clear()
                    binding.raza.text.clear()
                    binding.curp1.text.clear()
                    mostrar()
                }
                .addOnFailureListener {
                    AlertDialog.Builder(requireContext())
                        .setMessage(it.message)
                        .show()
                }

        }

        binding.btnCurp.setOnClickListener {
            mostrarCurp()
        }

        binding.btnNombre.setOnClickListener {
            mostrarNombre()
        }

        binding.btnCurp2.setOnClickListener {
            mostrarCurp2()
        }

        binding.btnRaza.setOnClickListener {
            mostrarRaza()
        }


        return root
    }
    fun mostrar(){
        FirebaseFirestore.getInstance()
            .collection("Mascota")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                for (documento in query!!){
                    var cadena = "Nombre Mascota: ${documento.getString("NOMBRE")}\n"+
                            "Raza Mascota: ${documento.getString("RAZA")}"
                    arreglo.add(cadena)
                    listaID.add(documento.id)
                }

                binding.lista.adapter= ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1,arreglo)

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

    fun mostrarNombre(){
        FirebaseFirestore.getInstance()
            .collection("Mascota")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                var nombre = binding.nombreM.text.toString()
                for (documento in query!!){
                    if (documento.getString("NOMBRE")==nombre){
                        var cadena = "Nombre Mascota: ${documento.getString("NOMBRE")}\n"+  "  "+
                                "Raza Mascota: ${documento.getString("RAZA")}"+"  "+
                                "Curp Propiertatio : ${documento.getString("CURP")}"
                        arreglo.add(cadena)
                        listaID.add(documento.id)
                    }

                }

                binding.lista.adapter= ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1,arreglo)

            }
    }

    fun mostrarRaza(){
        FirebaseFirestore.getInstance()
            .collection("Mascota")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                var raza = binding.raza.text.toString()
                for (documento in query!!){
                    if (documento.getString("RAZA")==raza){
                        var cadena = "Nombre Mascota: ${documento.getString("NOMBRE")}\n"+  "  "+
                                "Raza Mascota: ${documento.getString("RAZA")}"
                        arreglo.add(cadena)
                        listaID.add(documento.id)
                    }

                }

                binding.lista.adapter= ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1,arreglo)

            }
    }

    fun mostrarCurp2(){
        FirebaseFirestore.getInstance()
            .collection("Mascota")
            .addSnapshotListener{ query, error ->
                if (error!=null){
                    AlertDialog.Builder(requireContext())
                        .setMessage(error.message)
                        .show()
                    return@addSnapshotListener
                }

                val arreglo = ArrayList<String>()
                var curp = binding.curp1.text.toString()
                for (documento in query!!){
                    if (documento.getString("CURP")==curp){
                        var cadena = "Nombre Mascota: ${documento.getString("NOMBRE")}\n"+  "  "+
                                "Curp Propiertatio : ${documento.getString("CURP")}"
                        arreglo.add(cadena)
                        listaID.add(documento.id)
                    }

                }

                binding.lista.adapter= ArrayAdapter<String>(requireContext(),
                    R.layout.simple_list_item_1,arreglo)

            }
    }

    fun eliminar(idSeleccionado: String){
        val baseDatos = FirebaseFirestore.getInstance()
        baseDatos.collection("Mascota")
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
        var otraVentana = Intent(requireContext(), editarMasc::class.java)
        otraVentana.putExtra("idSeleccionado",idSeleccionado)
        startActivity(otraVentana)
    }


    fun mostrarCurp(){
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
                            "Nombre: ${documento.getString("NOMBRE")}"
                    arreglo.add(cad)
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