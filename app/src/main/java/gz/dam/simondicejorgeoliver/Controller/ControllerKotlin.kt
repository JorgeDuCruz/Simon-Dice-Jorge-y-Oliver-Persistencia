package gz.dam.simondicejorgeoliver.Controller

import gz.dam.simondicejorgeoliver.Record
import java.lang.Exception
import java.time.LocalDateTime
import java.util.Date

object ControllerKotlin : HandlerRecord{


    override fun setRecord(valorRecord: Int, fechaRecord: LocalDateTime): Int {
        try {
            Record.recordFeha = fechaRecord
            Record.recordPun = valorRecord
            return 1
        }catch(e: Exception){
            return 2
        }
    }

    override fun getRecord(): Record {
        return Record
    }


}