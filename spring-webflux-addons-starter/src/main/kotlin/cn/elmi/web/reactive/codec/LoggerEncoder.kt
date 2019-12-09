package cn.elmi.web.reactive.codec

import cn.elmi.web.reactive.decorator.consume
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.springframework.core.ResolvableType
import org.springframework.core.codec.Encoder
import org.springframework.core.codec.Hints.getLogPrefix
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.util.MimeType
import reactor.core.publisher.Flux

class LoggerEncoder<T>(
  private val encoder: Encoder<T>,
  private val logger: Logger
) : Encoder<T> by encoder {

    override fun encode(
      inputStream: Publisher<out T>,
      bufferFactory: DataBufferFactory,
      elementType: ResolvableType,
      mimeType: MimeType?,
      hints: MutableMap<String, Any>?
    ): Flux<DataBuffer> = encoder
        .encode(inputStream, bufferFactory, elementType, mimeType, hints)
        .consume { logger.body(getLogPrefix(hints), CodecType.Request, it) }

    override fun encodeValue(
      value: T,
      bufferFactory: DataBufferFactory,
      valueType: ResolvableType,
      mimeType: MimeType?,
      hints: MutableMap<String, Any>?
    ): DataBuffer = encoder.encodeValue(value, bufferFactory, valueType, mimeType, hints)
}