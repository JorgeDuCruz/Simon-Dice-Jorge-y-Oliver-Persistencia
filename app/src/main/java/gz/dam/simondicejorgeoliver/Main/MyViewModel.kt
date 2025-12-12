package gz.dam.simondicejorgeoliver.Main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gz.dam.simondicejorgeoliver.Controller.ControllerSQLite
import gz.dam.simondicejorgeoliver.Utility.Record
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter



class MyViewModel(application: Application): AndroidViewModel(application){
    val controllerSQLite = ControllerSQLite(application)
    val estadoActual: MutableStateFlow<Estados> = MutableStateFlow(Estados.INICIO)
    var numeroRandomGenerado = MutableStateFlow(0)

    val puntuacion = MutableStateFlow<Int>(0)

    var _record: Record = controllerSQLite.getRecord(getApplication())
    var _recordFecha: LocalDateTime = _record.recordFeha
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss") //Formato de texto en el que se guarda la fecha
    // https://developer.android.com/reference/java/time/format/DateTimeFormatter.html
    val record = MutableStateFlow<Int>(_record.recordPun)
    val recordData = MutableStateFlow<String>(_recordFecha.format(formatter)) // Variable para guardar la fecha del record




    var ronda = MutableStateFlow<Int?>(1)
    var botonPresionado = MutableStateFlow<Int?>(-1)


    var posicion = 0

    fun numeroRandom(){
        estadoActual.value = Estados.GENERANDO
        Log.d("ViewModel","Estado Generando")
        numeroRandomGenerado.value = (0..3).random()
        Log.d("ViewModel","Número aleatorio generado: $numeroRandomGenerado")
        actualizarNumero(numeroRandomGenerado.value)
    }

    fun actualizarNumero(numero:Int){
        Log.d("ViewModel","Actualizando el numero de la clase Datos")
        Datos.numero.add(numero)
        estadoActual.value = Estados.ADIVINANDO
        mostrarSecuencia(Datos.numero)

    }

    fun correcionOpcionElegida(numeroColor:Int): Boolean{
        Log.d("ViewModel","Combrobando si la opción escogida es correcta...")
        return if (numeroColor == Datos.numero[posicion]){
            Log.d("ViewModel","ES CORRECTO !")
            posicion++
            if (Datos.numero.size == posicion) {
                cambiarRonda()
            }
            puntuacion.value = puntuacion.value.plus(1)

            true
        }else{
            Log.d("ViewModel","ERROR, HAS PERDIDO")
            derrota()
            false
        }
    }

    fun mostrarSecuencia(secuencia: ArrayList<Int>){
        Log.d("ViewModel","Estado Adivinando, secuencia: $secuencia")
        viewModelScope.launch {
            for (boton in secuencia){
                botonPresionado.value=boton
                delay(800)
            }
        }
    }

    fun continuarSecuencia(){
        botonPresionado.value=-1

    }

    fun cambiarRonda(){
        posicion=0
        ronda.value = ronda.value?.plus(1)

        numeroRandom()
    }

    fun derrota(){
        record.value = _record.recordPun
        if (record.value < puntuacion.value){
            actualizarRecord()
        }
        puntuacion.value = 0
        posicion=0
        ronda.value = 1
        estadoActual.value = Estados.INICIO
        Datos.numero = ArrayList()

    }

    /**
     * Función que actualiza los datos del record de la viewModel y luego los manda actualizar en la clase Datos.
     */
    fun actualizarRecord(){
        record.value = puntuacion.value
        controllerSQLite.setRecord(record.value, LocalDateTime.now(),getApplication())
        _record = controllerSQLite.getRecord(getApplication())
        _recordFecha = _record.recordFeha
        recordData.value = _recordFecha.format(formatter)
    }
}