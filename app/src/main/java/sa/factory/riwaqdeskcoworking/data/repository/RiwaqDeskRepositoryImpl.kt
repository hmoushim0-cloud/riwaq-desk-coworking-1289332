package sa.factory.riwaqdeskcoworking.data.repository

import sa.factory.riwaqdeskcoworking.domain.repository.RiwaqDeskRepository
import sa.factory.riwaqdeskcoworking.hardware.HardwareSensorDriver
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskOrder
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskTelemetry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RiwaqDeskRepositoryImpl(
    private val sensorDriver: HardwareSensorDriver
) : RiwaqDeskRepository {

    private val _catalog = MutableStateFlow<List<RiwaqDeskItem>>(listOf(
        RiwaqDeskItem("1", "الباقة التنفيذية المعتمدة", "تكامل مباشر مع 13.56 MHz RFID Turnstile Access + TOTP Pass", 180.0, "معتمد", "الخدمات الرئيسية"),
        RiwaqDeskItem("2", "خدمة المعالجة الفورية", "أداء أصيل بسرعة 60 إطار في الثانية", 95.0, "فوري", "المراقبة الحية"),
        RiwaqDeskItem("3", "باقة الصيانة والدعم السحابي", "تشفير كامل للبيانات المحلية ومزامنة آمنة", 250.0, "شامل", "التقارير الضريبية")
    ))
    override val catalogFlow: Flow<List<RiwaqDeskItem>> = _catalog.asStateFlow()

    private val _cart = MutableStateFlow<List<RiwaqDeskItem>>(emptyList())
    override val activeCart: StateFlow<List<RiwaqDeskItem>> = _cart.asStateFlow()

    override val telemetryFlow: Flow<RiwaqDeskTelemetry> = sensorDriver.observePeripheralStream()
        .map { frame ->
            RiwaqDeskTelemetry(
                peripheralName = frame.peripheralName,
                status = frame.connectionState,
                liveReading = frame.livePayload,
                batteryPct = 96,
                signalDbm = -54
            )
        }

    override fun addToCart(item: RiwaqDeskItem) {
        val current = _cart.value.toMutableList()
        current.add(item)
        _cart.value = current
    }

    override fun removeFromCart(item: RiwaqDeskItem) {
        val current = _cart.value.toMutableList()
        current.remove(item)
        _cart.value = current
    }

    override fun clearCart() {
        _cart.value = emptyList()
    }

    override suspend fun checkout(paymentMethod: String): RiwaqDeskOrder {
        val currentItems = _cart.value.toList()
        val subtotal = currentItems.sumOf { it.priceSar }
        val vat = subtotal * 0.15
        val total = subtotal + vat
        val order = RiwaqDeskOrder(
            orderId = "ORD-${System.currentTimeMillis() % 100000}",
            items = currentItems,
            subtotalSar = subtotal,
            vatSar = vat,
            totalSar = total,
            paymentMethod = paymentMethod
        )
        clearCart()
        return order
    }

    override suspend fun refreshCatalog() {
        // Offline-first sync logic
    }
}
