package gz.dam.simondicejorgeoliver.Model.Room

import androidx.room.Database
import androidx.room.RoomDatabase
import gz.dam.simondicejorgeoliver.Utility.RecordEntity

@Database(entities = [RecordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}