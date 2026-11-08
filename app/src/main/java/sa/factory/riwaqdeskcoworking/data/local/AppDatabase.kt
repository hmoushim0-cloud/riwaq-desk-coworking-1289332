package sa.factory.riwaqdeskcoworking.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RiwaqDeskEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coworkingDao(): RiwaqDeskDao
}
