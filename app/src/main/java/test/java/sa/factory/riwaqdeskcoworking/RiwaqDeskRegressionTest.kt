package sa.factory.riwaqdeskcoworking

import sa.factory.riwaqdeskcoworking.data.repository.RiwaqDeskRepositoryImpl
import sa.factory.riwaqdeskcoworking.hardware.HardwareSensorDriver
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class RiwaqDeskRegressionTest {

    @Test
    fun testCheckoutCalculationAndTax() = runBlocking {
        val sensorDriver = HardwareSensorDriver()
        val repository = RiwaqDeskRepositoryImpl(sensorDriver)

        val item1 = RiwaqDeskItem("t1", "خدمة تجريبية أ", "الوصف", 100.0, "معتمد", "عام")
        val item2 = RiwaqDeskItem("t2", "خدمة تجريبية ب", "الوصف", 200.0, "معتمد", "عام")

        repository.addToCart(item1)
        repository.addToCart(item2)

        val order = repository.checkout("Mada EMV")

        assertEquals(300.0, order.subtotalSar, 0.01)
        assertEquals(45.0, order.vatSar, 0.01)
        assertEquals(345.0, order.totalSar, 0.01)
        assertEquals(0, repository.activeCart.value.size)
    }
}
