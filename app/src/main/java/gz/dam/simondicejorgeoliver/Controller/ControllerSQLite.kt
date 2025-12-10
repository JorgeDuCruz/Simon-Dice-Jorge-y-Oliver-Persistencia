package gz.dam.simondicejorgeoliver.Controller

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.util.Log
import gz.dam.simondicejorgeoliver.Utility.ContratoSQLite
import gz.dam.simondicejorgeoliver.Utility.ContratoSQLite.FeedEntry
import gz.dam.simondicejorgeoliver.Utility.Record
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ControllerSQLite(context: Context) : HandlerRecord{
    val dbHelper : ContratoSQLite.SQLiteHelper = ContratoSQLite.SQLiteHelper(context)
    val dbWriter = dbHelper.writableDatabase
    val dbReader = dbHelper.readableDatabase
    val TAG = "SQLite"
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss") //Formato de texto en el que se guarda la fecha

    override fun setRecord(
        valorRecord: Int,
        fechaRecord: LocalDateTime,
        context: Context
    ): Int {
        var fechaString = fechaRecord.format(formatter)
        val values = ContentValues().apply {
            put(FeedEntry.COLUMN_NAME_PUNTUACION,valorRecord)
            put(FeedEntry.COLUMN_NAME_FECHA,fechaString)
        }
        Log.d(TAG,"Creada fila")


        // Insert the new row, returning the primary key value of the new row
        val newRowId = dbWriter?.insert(FeedEntry.TABLE_NAME, null, values)
        Log.d(TAG,"insertados datos $newRowId")
        if (newRowId!!<0){
            return -1
        }
        else return 1
    }

    override fun getRecord(context: Context): Record {
        val projection = arrayOf(FeedEntry.COLUMN_NAME_PUNTUACION, FeedEntry.COLUMN_NAME_FECHA)

        Log.d(TAG,"Ordenando select")

        val sortOrder = "${FeedEntry.COLUMN_NAME_PUNTUACION} DESC"

        val cursor = dbReader.query(
            FeedEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        Log.d(TAG,"valores recibidos")


        var puntuacion = 0
        var fecha = ""

        with(cursor) {
            while (moveToNext()) {
                puntuacion = getInt(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_PUNTUACION))
                Log.d(TAG,"Valor = $puntuacion")
                fecha = getString(getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_FECHA))
                Log.d(TAG,"Valor = $fecha")
            }
        }
        Log.d(TAG,"Valores = $puntuacion , $fecha")

        cursor.close()
        Record.recordFeha = LocalDateTime.parse(fecha, ControllerPreferences.formatter) // Convertir una string a LocalDateTime con un formato unificado
        Record.recordPun = puntuacion
        return Record
    }

}