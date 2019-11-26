package cn.elmi.web.reactive.decorator

import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import reactor.core.publisher.Mono

class TracingServerHttpResponseDecorator(delegate: ServerHttpResponse, private val logTag: String) : ServerHttpResponseDecorator(delegate) {

    private val logger = LoggerFactory.getLogger(TracingServerHttpResponseDecorator::class.java)

    override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> = super.writeWith(body.consume { tracing(it) })

    private fun tracing(body: String? = null) {
        logger.info("""${"\n"}
            |Server response:
            |   trackId:    $logTag
            |   status:     $statusCode
            |   header:     $headers
            |   cookies:    $cookies
            |   body:       $body
        """.trimMargin())
    }
}