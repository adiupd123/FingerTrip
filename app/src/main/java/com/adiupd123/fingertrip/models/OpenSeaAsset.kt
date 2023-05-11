package com.adiupd123.fingertrip.models

data class OpenSeaAsset (
    val tokenId: String,
    val imageUrl: String,
    val name: String,
    val description: String,
    val assetContractAddress: String
)