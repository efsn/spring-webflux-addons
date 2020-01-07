package cn.elmi.web.reactive.api

import cn.elmi.web.reactive.decorator.withLogger
import com.fasterxml.jackson.databind.JsonNode
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class TestRoute {

    private val logger = LoggerFactory.getLogger(TestRoute::class.java)

    @Bean
    fun webClient(): WebClient =
        WebClient.builder()
            .defaultHeader("Accept", "application/vnd.github.v3+json")
            .baseUrl("https://api.github.com")
            .withLogger(logger)
            .build()

    @Bean
    fun apiRouter(webClient: WebClient) = coRouter {
        accept(APPLICATION_JSON).nest {
            POST("/test/fn") {
                val cmd = it.awaitBody<TestCmd>()
                ok().contentType(APPLICATION_JSON).bodyValueAndAwait(mapOf("${cmd.hi}" to "world"))
            }

            GET("/log") {
                val body = webClient.get().uri("/orgs/octokit/repos").retrieve().awaitBody<JsonNode>()
                ok().contentType(APPLICATION_JSON).bodyValueAndAwait(body)
            }
        }
    }
}