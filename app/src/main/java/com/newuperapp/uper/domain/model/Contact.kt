package com.newuperapp.uper.domain.model

data class Contact(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val mutualFriendsCount: Int,
    val isSelected: Boolean = false
)
