package gz.dam.simondicejorgeoliver.Utility

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,

    @ColumnInfo(name = "Puntuación") val puntuacion: Int?,
    @ColumnInfo(name = "Fecha") val fecha: String?
)