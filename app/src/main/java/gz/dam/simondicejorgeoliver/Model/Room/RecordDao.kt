package gz.dam.simondicejorgeoliver.Model.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import gz.dam.simondicejorgeoliver.Utility.RecordEntity

@Dao
interface RecordDao {

    @Query("SELECT * FROM RecordEntity")
    fun getAll(): List<RecordEntity>

    @Query("SELECT * FROM RecordEntity WHERE id IN (:RecordIds)")
    fun loadAllByIds(RecordIds: IntArray): List<RecordEntity>

    @Query("SELECT Puntuación, Fecha FROM RecordEntity ORDER BY Puntuación DESC LIMIT 1")
    fun getMaxRecord(): RecordEntity

    @Insert
    fun insertAll(vararg Records: RecordEntity)

    @Delete
    fun delete(RecordEntity: RecordEntity)
}