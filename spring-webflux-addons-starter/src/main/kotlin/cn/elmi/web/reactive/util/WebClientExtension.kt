package cn.elmi.web.reactive.util

import cn.elmi.web.reactive.codec.LoggerDecoder
import cn.elmi.web.reactive.codec.LoggerEncoder
import org.slf4j.Logger
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

const val tenMb: Int = 10 * 1024 * 1024

fun WebClient.Builder.logWith(logger: Logger) = exchangeStrategies(ExchangeStrategies.builder().codecs {
    it.defaultCodecs().jackson2JsonEncoder(LoggerEncoder(Jackson2JsonEncoder(), logger))
    it.defaultCodecs().jackson2JsonDecoder(LoggerDecoder(Jackson2JsonDecoder().apply { maxInMemorySize = tenMb }, logger))
}.build())