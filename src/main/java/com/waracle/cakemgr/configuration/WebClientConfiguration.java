package com.waracle.cakemgr.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfiguration {

  @Bean
  public WebClient webClient() {
    final HttpClient httpClient = HttpClient
            .create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(connection -> {
              connection.addHandlerLast(new ReadTimeoutHandler(5, TimeUnit.SECONDS));
              connection.addHandlerLast(new WriteTimeoutHandler(5, TimeUnit.SECONDS));
            });
    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
  }
}
