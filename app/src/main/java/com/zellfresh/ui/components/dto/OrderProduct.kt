package com.zellfresh.ui.components.dto

data class OrderProduct(
    val name: String,
    val unit: String,
    val imageUrl: String,
    val description: String,
) {
    companion object {
        val default = OrderProduct(
            name = "",
            unit = "??",
            imageUrl = "",
            description = ""
        )
    }
}