package sa.factory.riwaqdeskcoworking.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RiwaqDeskDao {
    @Query("SELECT * FROM coworking_items_cache ORDER BY id ASC")
    fun observeAllItems(): Flow<List<RiwaqDeskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<RiwaqDeskEntity>)

    @Query("DELETE FROM coworking_items_cache")
    suspend fun clearAll()
}
