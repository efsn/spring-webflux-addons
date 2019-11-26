import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.hook.DelayingShutdownHook
import kotlin.text.Charsets

import static ch.qos.logback.classic.Level.INFO

scan("60 seconds")
def LOG_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] [%X{uniqueTrackingNumber}] %-5level %logger{64} - %msg%n"
appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "${LOG_PATTERN}"
        charset = Charsets.UTF_8
    }
}
addShutdownHook { DelayingShutdownHook }
root(INFO, ["CONSOLE"])