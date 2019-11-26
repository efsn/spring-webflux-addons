package cn.elmi.web.reactive.filter

import cn.elmi.web.reactive.decorator.TracingServerHttpRequestDecorator
import cn.elmi.web.reactive.decorator.TracingServerHttpResponseDecorator
import org.slf4j.MDC
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class TracingFilter : WebFilter {

    companion object {
        const val TRACK_ID = "uniqueTrackingNumber"
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        MDC.put(TRACK_ID, exchange.logPrefix.substring(1, exchange.logPrefix.length - 2))

        return chain.filter(
            exchange
                .mutate()
                .request(TracingServerHttpRequestDecorator(exchange.request, exchange.logPrefix))
                .response(TracingServerHttpResponseDecorator(exchange.response, exchange.logPrefix))
                .build()
        )
    }
}