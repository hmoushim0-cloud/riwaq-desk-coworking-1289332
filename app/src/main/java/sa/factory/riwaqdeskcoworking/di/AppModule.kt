package sa.factory.riwaqdeskcoworking.di

import sa.factory.riwaqdeskcoworking.data.repository.RiwaqDeskRepositoryImpl
import sa.factory.riwaqdeskcoworking.domain.repository.RiwaqDeskRepository
import sa.factory.riwaqdeskcoworking.domain.usecase.*
import sa.factory.riwaqdeskcoworking.hardware.HardwareSensorDriver
import sa.factory.riwaqdeskcoworking.ui.viewmodel.RiwaqDeskViewModel

class DiContainer {
    val sensorDriver = HardwareSensorDriver()
    val repository: RiwaqDeskRepository = RiwaqDeskRepositoryImpl(sensorDriver)

    val getCatalogUseCase = GetCatalogUseCase(repository)
    val manageCartUseCase = ManageCartUseCase(repository)
    val processCheckoutUseCase = ProcessCheckoutUseCase(repository)
    val observeHardwareTelemetryUseCase = ObserveHardwareTelemetryUseCase(repository)

    fun provideViewModel(): RiwaqDeskViewModel = RiwaqDeskViewModel(
        getCatalogUseCase,
        manageCartUseCase,
        processCheckoutUseCase,
        observeHardwareTelemetryUseCase
    )
}
