//package com.example.medease.presentation.viewmodels
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.za_zoo_find_refreshments.domain.usecases.GetProductsUseCase
//import com.example.za_zoo_find_refreshments.presentation.viewmodels.events.HomeEvent
//import com.example.za_zoo_find_refreshments.presentation.viewmodels.events.ProductsState
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val getProductsUseCase: GetProductsUseCase
//) : ViewModel() {
//
//    private val _state = MutableStateFlow(ProductsState())
//    val state = _state.asStateFlow()
//
//    init {
//        getProducts(query = state.value.selectedProduct, location = state.value.selectedLocation)
//    }
//    fun onEvent(event: HomeEvent){
//        when(event){
//            is HomeEvent.FilterProducts -> {
//                _state.update { it.copy(selectedProduct = event.query) }
//                getProducts(query = event.query, location = state.value.selectedLocation)
//            }
//            is HomeEvent.ChangeLocation -> {
//                _state.update { it.copy(selectedLocation = event.location) }
//                getProducts(query = state.value.selectedProduct, location = event.location)
//            }
//            is HomeEvent.AddToCart -> {
//                _state.update {
//                    it.copy(
//                        addToCart = it.addToCart.plus(event.cartItem)
//                    )
//                }
//            }
//
//            is HomeEvent.AddProductImageToCart -> {
//                _state.update {
//                    it.copy(
//                        cartItemImages = it.cartItemImages.plus(event.imageUrl)
//                    )
//                }
//            }
//        }
//    }
//    private fun getProducts(query: String, location: String) {
//        viewModelScope.launch {
//            _state.update { it.copy(loading = true) }
//            getProductsUseCase.getProducts(query = query, location = location).onRight { products ->
//                Log.d("check 1", "getProducts loading: ${state.value.loading}")
//                delay(3000)
//                _state.update {
//                    it.copy(
//                        products = products.detailResultsThisPage,
//                        loading = false
//                    )
//                }
//                Log.d("check", "getProducts loading: ${state.value.loading}")
//                Log.d("check", "getProducts Name: ${state.value.products[0].name}")
//            }.onLeft {
//                _state.update { it.copy(error = it.error, loading = false) }
//                Log.d("check", "getProducts Error: ${it.error}")
//            }
//        }
//    }
//}