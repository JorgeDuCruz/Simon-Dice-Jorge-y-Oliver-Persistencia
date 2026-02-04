package gz.dam.simondicejorgeoliver.Controller

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import gz.dam.simondicejorgeoliver.Model.Room.AppDatabase
import gz.dam.simondicejorgeoliver.Utility.Record
import gz.dam.simondicejorgeoliver.Utility.RecordEntity
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ControllerRoomSQLite(applicationContext: Application) : HandlerRecord {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss") //Formato de texto en el que se guarda la fecha

    val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "RoomRecord"
    ).allowMainThreadQueries().build()
    val recordDao = db.recordDao()


    override fun setRecord(
        valorRecord: Int,
        fechaRecord: LocalDateTime,
        context: Context
    ): Int {
        try {

            val record = RecordEntity(
                id = null,
                puntuacion = valorRecord,
                fecha = fechaRecord.format(formatter),
                nombre = "Juaco"
            )
            recordDao.insertAll(record)
            return 1
        }catch (e: Exception){
            Log.d("Prueba Room SQLite","Error al insertar $e")
            return -1
        }
    }

    override fun getRecord(context: Context): Record {
        val record = recordDao.getMaxRecord()

        val fecha: LocalDateTime
        val puntuacion: Int
        val nombre : String

        if (record != null){
            fecha = LocalDateTime.parse(record.fecha, formatter)
            if (record.puntuacion != null) puntuacion = record.puntuacion
            else puntuacion = 0
            if (record.nombre != null) nombre = record.nombre
            else nombre = "Jośe"
        }
        else{
            fecha = LocalDateTime.parse("11/11/2011 11:11:11",formatter)
            puntuacion = 0
            nombre = "Jośe"
        }
        Record.recordPun = puntuacion
        Record.recordFeha = fecha
        Record.nombre = nombre
        return Record
    }

}