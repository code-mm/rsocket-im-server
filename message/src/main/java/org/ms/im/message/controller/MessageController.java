package org.ms.im.message.controller;

import com.bdlbsc.common.Result;
import com.bdlbsc.common.im.MessageText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;

@Controller
@Slf4j
public class MessageController {


    @MessageMapping("fire-and-forget")
    public void fireAndForget(String request) {

    }

    @MessageMapping("request-response")
    Result<MessageText> requestResponse(MessageText request) {

        return null;
    }

    @MessageMapping("stream")
    Flux<MessageText> stream(MessageText request) {
        log.info("Received stream request: {}", request);
        return Flux
                // create a new indexed Flux emitting one element every second
                .interval(Duration.ofSeconds(1))
                // create a Flux of new Messages using the indexed Flux
                .map(index -> new MessageText())
                // use the Flux logger to output each flux event
                .log();
    }

    /**
     * This @MessageMapping is intended to be used "stream --> stream" style.
     * When a new stream of CommandRequests is received, a new stream of Messages is started and returned to the client.
     * @param requests
     * @return
     */
    @MessageMapping("channel")
    Flux<MessageText> channel(Flux<MessageText> requests) {
        log.info("Received channel request (stream) at {}", Instant.now());
        return requests
                // log what has been received
                .log()
                // then every 1 second per element received
                .delayElements(Duration.ofSeconds(1))
                // create a new Flux with one Message for each element (numbered)
                .map(input -> new MessageText())
                // log what is being sent
                .log();
    }

}
