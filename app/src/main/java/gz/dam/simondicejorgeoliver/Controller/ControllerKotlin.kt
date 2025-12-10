package gz.dam.simondicejorgeoliver.Controller

import android.content.Context
import gz.dam.simondicejorgeoliver.Utility.Record
import java.lang.Exception
import java.time.LocalDateTime
import java.util.Date

object ControllerKotlin : HandlerRecord{

    override fun setRecord(
        valorRecord: Int,
        fechaRecord: LocalDateTime,
        context: Context
    ): Int {
        try {
            Record.recordFeha = fechaRecord
            Record.recordPun = valorRecord
            return 1
        }catch(e: Exception){
            return 2
        }
    }

    override fun getRecord(context: Context): Record {
        return Record
    }


}