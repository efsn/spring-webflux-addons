package cn.elmi.web.reactive.codec

import org.slf4j.Logger
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse

fun Logger.body(logTag: String, type: CodecType, body: String) = info("$logTag $type body: $body")
fun Logger.requestMeta(request: ClientRequest) = info("""${"\n"}
    |Upstream request :
    |   trackId:    ${request.logPrefix()}
    |   method:     ${request.method()}
    |   url:        ${request.url()}
    |   header:     ${request.headers()}
    |   cookies:    ${request.cookies()}
    |   body:       TODO
""".trimMargin())

fun Logger.responseMeta(logTag: String, response: ClientResponse) = info("""${"\n"}
    |Upstream response:
    |   trackId:    $logTag
    |   status:     ${response.statusCode()}
    |   header:     ${response.headers()}
    |   body:       TODO
""".trimMargin())