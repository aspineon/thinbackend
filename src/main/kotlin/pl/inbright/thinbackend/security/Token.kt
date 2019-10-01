package pl.inbright.thinbackend.security

import com.fasterxml.jackson.annotation.JsonProperty

data class Token(@JsonProperty("token") val value: String, val expirationTimeInSeconds: Long)