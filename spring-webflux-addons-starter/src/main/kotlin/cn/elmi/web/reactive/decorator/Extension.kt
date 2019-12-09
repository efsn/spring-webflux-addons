package cn.elmi.web.reactive.decorator

import cn.elmi.web.reactive.codec.LoggerDecoder
import cn.elmi.web.reactive.codec.LoggerEncoder
import cn.elmi.web.reactive.codec.requestMeta
import cn.elmi.web.reactive.codec.responseMeta
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.MDC
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.io.ByteArrayOutputStream
import java.nio.channels.Channels

fun <T : DataBuffer> Publisher<T>.consume(consumer: (String) -> Unit): Flux<T> {
    val buffer = ByteArrayOutputStream()
    return toFlux()
        .doOnNext { Channels.newChannel(buffer).write(it.asByteBuffer().asReadOnlyBuffer()) }
        .doOnComplete {
            buffer.flush()
            consumer(buffer.toString(Charsets.UTF_8.name()))
        }
        .doFinally { buffer.close() }
}

fun WebClient.Builder.withLogger(logger: Logger): WebClient.Builder =
    exchangeStrategies(
        ExchangeStrategies.builder()
            .codecs {
                it.defaultCodecs().jackson2JsonEncoder(LoggerEncoder(Jackson2JsonEncoder(), logger))
                it.defaultCodecs().jackson2JsonDecoder(LoggerDecoder(Jackson2JsonDecoder(), logger))
            }.build()
    ).filter { request, next ->

        val rq = ClientRequest.from(request)
            .attribute(ClientRequest.LOG_ID_ATTRIBUTE, MDC.get("uniqueTrackingNumber"))
            .build()

        next.exchange(rq)
            .also { logger.requestMeta(rq) }
            .doOnNext { logger.responseMeta(rq.logPrefix(), it) }
    }