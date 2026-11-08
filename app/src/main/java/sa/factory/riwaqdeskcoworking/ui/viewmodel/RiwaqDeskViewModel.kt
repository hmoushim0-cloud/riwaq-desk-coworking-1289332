package sa.factory.riwaqdeskcoworking.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import sa.factory.riwaqdeskcoworking.domain.usecase.*
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskItem
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskOrder
import sa.factory.riwaqdeskcoworking.model.RiwaqDeskTelemetry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RiwaqDeskUiState(
    val catalog: List<RiwaqDeskItem> = emptyList(),
    val filteredCatalog: List<RiwaqDeskItem> = emptyList(),
    val selectedCategory: String = "الكل",
    val cart: List<RiwaqDeskItem> = emptyList(),
    val telemetry: RiwaqDeskTelemetry? = null,
    val lastOrder: RiwaqDeskOrder? = null,
    val isCheckingOut: Boolean = false,
    val activeTab: String = "home"
)

class RiwaqDeskViewModel(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val processCheckoutUseCase: ProcessCheckoutUseCase,
    private val observeHardwareTelemetryUseCase: ObserveHardwareTelemetryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RiwaqDeskUiState())
    val uiState: StateFlow<RiwaqDeskUiState> = _uiState.asStateFlow()

    init {
        // Observe Catalog
        getCatalogUseCase().onEach { list ->
            _uiState.update { it.copy(catalog = list, filteredCatalog = list) }
        }.launchIn(viewModelScope)

        // Observe Cart
        manageCartUseCase.cart.onEach { cartItems ->
            _uiState.update { it.copy(cart = cartItems) }
        }.launchIn(viewModelScope)

        // Observe Telemetry
        observeHardwareTelemetryUseCase().onEach { telem ->
            _uiState.update { it.copy(telemetry = telem) }
        }.launchIn(viewModelScope)
    }

    fun selectCategory(category: String) {
        _uiState.update { state ->
            val filtered = if (category == "الكل") state.catalog else state.catalog.filter { it.category == category }
            state.copy(selectedCategory = category, filteredCatalog = filtered)
        }
    }

    fun switchTab(tab: String) {
        _uiState.update { it.copy(activeTab = tab) }
    }

    fun addToCart(item: RiwaqDeskItem) = manageCartUseCase.add(item)
    fun removeFromCart(item: RiwaqDeskItem) = manageCartUseCase.remove(item)

    fun checkout() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCheckingOut = true) }
            val order = processCheckoutUseCase()
            _uiState.update { it.copy(isCheckingOut = false, lastOrder = order, activeTab = "checkout") }
        }
    }
}
