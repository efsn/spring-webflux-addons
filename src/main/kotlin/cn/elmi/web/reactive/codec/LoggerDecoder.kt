package cn.elmi.web.reactive.codec

import cn.elmi.web.reactive.decorator.consume
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.springframework.core.ResolvableType
import org.springframework.core.codec.Decoder
import org.springframework.core.codec.Hints.getLogPrefix
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.util.MimeType
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class LoggerDecoder<T>(
  private val decoder: Decoder<T>,
  private val logger: Logger
) : Decoder<T> by decoder {

    override fun decode(
      inputStream: Publisher<DataBuffer>,
      elementType: ResolvableType,
      mimeType: MimeType?,
      hints: MutableMap<String, Any>?
    ): Flux<T> =
        inputStream
            .consume { logger.body(getLogPrefix(hints), CodecType.Response, it) }
            .let { decoder.decode(it, elementType, mimeType, hints) }

    override fun decodeToMono(
      inputStream: Publisher<DataBuffer>,
      elementType: ResolvableType,
      mimeType: MimeType?,
      hints: MutableMap<String, Any>?
    ): Mono<T> =
        inputStream
            .consume { logger.body(getLogPrefix(hints), CodecType.Response, it) }
            .let { decoder.decodeToMono(it, elementType, mimeType, hints) }

    override fun decode(buffer: DataBuffer, targetType: ResolvableType, mimeType: MimeType?, hints: MutableMap<String, Any>?): T =
        decoder.decode(buffer, targetType, mimeType, hints)
}