package cn.elmi.web.reactive.decorator

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod.*
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpRequestDecorator

class TracingServerHttpRequestDecorator(delegate: ServerHttpRequest, private val logTag: String) : ServerHttpRequestDecorator(delegate) {

    private val logger = LoggerFactory.getLogger(TracingServerHttpRequestDecorator::class.java)

    init {
        method?.takeIf { setOf(POST, PUT, PATCH).contains(it) } ?: tracing()
    }

    override fun getBody() = super.getBody().consume { tracing(it) }

    private fun tracing(body: String? = null) {
        logger.info("""${"\n"}
            |Client request:
            |   trackId:    $logTag
            |   remoteIP:   $remoteAddress
            |   method:     $method
            |   url:        $uri
            |   header:     $headers
            |   cookies:    $cookies
            |   body:       ${body?.replace("[\n\t]".toRegex(), "")}
        """.trimMargin())
    }
}