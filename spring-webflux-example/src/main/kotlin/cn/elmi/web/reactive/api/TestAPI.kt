package cn.elmi.web.reactive.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.kotlin.core.publisher.toMono

@RestController
class TestAPI(
  private val webClient: WebClient
) {

    @PostMapping("/test")
    fun hello(@RequestBody cmd: TestCmd) = mapOf("${cmd.hi}" to "world").toMono()
}

class TestCmd(val hi: String)