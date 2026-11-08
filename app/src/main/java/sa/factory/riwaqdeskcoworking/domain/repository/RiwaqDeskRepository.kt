package sa.factory.riwaqdeskcoworking.domain.repository

import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskOrder
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskTelemetry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RiwaqDeskRepository {
    val catalogFlow: Flow<List<RiwaqDeskItem>>
    val activeCart: StateFlow<List<RiwaqDeskItem>>
    val telemetryFlow: Flow<RiwaqDeskTelemetry>
    
    fun addToCart(item: RiwaqDeskItem)
    fun removeFromCart(item: RiwaqDeskItem)
    fun clearCart()
    suspend fun checkout(paymentMethod: String): RiwaqDeskOrder
    suspend fun refreshCatalog()
}
