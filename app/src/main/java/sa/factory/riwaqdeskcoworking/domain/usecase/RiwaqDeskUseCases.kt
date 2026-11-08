package sa.factory.riwaqdeskcoworking.domain.usecase

import sa.factory.riwaqdeskcoworking.domain.repository.RiwaqDeskRepository
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskOrder
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskTelemetry
import kotlinx.coroutines.flow.Flow

class GetCatalogUseCase(private val repository: RiwaqDeskRepository) {
    operator fun invoke(): Flow<List<RiwaqDeskItem>> = repository.catalogFlow
}

class ManageCartUseCase(private val repository: RiwaqDeskRepository) {
    val cart = repository.activeCart
    fun add(item: RiwaqDeskItem) = repository.addToCart(item)
    fun remove(item: RiwaqDeskItem) = repository.removeFromCart(item)
    fun clear() = repository.clearCart()
}

class ProcessCheckoutUseCase(private val repository: RiwaqDeskRepository) {
    suspend operator fun invoke(paymentMethod: String = "Mada Contactless EMV"): RiwaqDeskOrder =
        repository.checkout(paymentMethod)
}

class ObserveHardwareTelemetryUseCase(private val repository: RiwaqDeskRepository) {
    operator fun invoke(): Flow<RiwaqDeskTelemetry> = repository.telemetryFlow
}
