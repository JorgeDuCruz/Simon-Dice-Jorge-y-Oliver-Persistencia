package gz.dam.simondicejorgeoliver.Controller

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import gz.dam.simondicejorgeoliver.Utility.Record
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ControllerPreferences: HandlerRecord {
    private const val PREFS_NAME = "preferencias_app"
    private const val KEY_RECORD = "record"
    private const val KEY_RECORD_FECHA = "record_fecha"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss") //Formato de texto en el que se guarda la fecha

    /**
     * Función para guardar el record en con shared preference
     * @param valorRecord puntuación del record
     * @param fechaRecord fecha a la que se hizo el record
     * @param context Contexto de la aplicación (requisito del SharedPreferences)
     * https://developer.android.com/training/data-storage/shared-preferences?hl=es-419
     */
    override fun setRecord(
        valorRecord: Int,
        fechaRecord: LocalDateTime,
        context: Context
    ): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putInt(KEY_RECORD, valorRecord)
            putString(KEY_RECORD_FECHA, fechaRecord.format(formatter) )
        }
        return 1
    }

    /**
     * Función para guardar el record en con shared preference
     * @param context Contexto de la aplicación (requisito del SharedPreferences)
     * https://developer.android.com/training/data-storage/shared-preferences?hl=es-419
     */
    override fun getRecord(context: Context): Record {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val pun = sharedPreferences.getInt(KEY_RECORD, 0)
        val fec = sharedPreferences.getString(KEY_RECORD_FECHA, "11/11/2011 11:11:11") // Recojer el string de la fecha con un dato por defecto con el formato correcto
        Log.d("PREFS","$fec")
        Record.recordFeha = LocalDateTime.parse(fec,formatter) // Convertir una string a LocalDateTime con un formato unificado
        Record.recordPun = pun
        return Record
    }


}