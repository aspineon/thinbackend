package pl.inbright.thinbackend.common

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LocationHeaderPrefixFilter : AbstractGatewayFilterFactory<LocationHeaderPrefixFilter.Config>(Config::class.java) {

    @Value("\${thinBackend.baseEndpoint}")
    lateinit var baseEndpoint: String
    @Value("\${thinBackend.apiPrefix}")
    lateinit var apiPrefix: String

    private val locationHeader = "Location"

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val request = exchange.request
            chain.filter(exchange.mutate().request(request).build()).then(Mono.fromRunnable {
                exchange.response.headers[locationHeader]?.let {
                    exchange.response.headers[locationHeader] = it[0].replace(baseEndpoint, "$apiPrefix/$baseEndpoint")
                }
            })
        }
    }

    class Config

}