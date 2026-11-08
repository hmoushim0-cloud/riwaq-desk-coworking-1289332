package sa.factory.riwaqdeskcoworking.data.remote

import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskOrder

interface RiwaqDeskApiService {
    suspend fun fetchCatalog(): List<RiwaqDeskItem>
    suspend fun submitOrder(order: RiwaqDeskOrder): Boolean
}
