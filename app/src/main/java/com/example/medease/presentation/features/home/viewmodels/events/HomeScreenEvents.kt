//package com.example.za_zoo_find_refreshments.presentation.viewmodels.events
//
//import com.example.za_zoo_find_refreshments.domain.model.DetailResult
//
//sealed class HomeEvent {
//    data class FilterProducts(val query: String): HomeEvent()
//    data class AddProductImageToCart(val imageUrl: String): HomeEvent()
//    data class ChangeLocation(val location: String): HomeEvent()
//    data class AddToCart(val cartItem: DetailResult): HomeEvent()
//}