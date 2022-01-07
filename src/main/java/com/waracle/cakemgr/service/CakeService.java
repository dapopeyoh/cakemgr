package com.waracle.cakemgr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.dto.CakeRequest;
import com.waracle.cakemgr.dto.client.CakeClientResponse;
import com.waracle.cakemgr.exception.CakeNotFoundException;
import com.waracle.cakemgr.exception.InvalidCakeException;
import com.waracle.cakemgr.model.Cake;
import com.waracle.cakemgr.repository.CakeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CakeService {
  private final CakeRepository cakeRepository;
  private final WebClient webClient;

  @Value("${cake.url}")
  private String cakeUrl;

  @EventListener
  public void init(ApplicationReadyEvent event) {
    Mono<String> cakeMono = webClient.get()
            .uri(cakeUrl)
            .retrieve()
            .bodyToMono(String.class);

    cakeMono.subscribe(cakeString -> {

      List<CakeClientResponse> cakeResponseList = convertCakeStringToList(cakeString);
      cakeResponseList.forEach(cakeResponse -> {
        CakeRequest cakeRequest = new CakeRequest(cakeResponse.getTitle(), cakeResponse.getDesc(), cakeResponse.getImage());
        try {
          addCake(cakeRequest);
        } catch (RuntimeException ex) {
          log.info("duplicate... " + cakeRequest.getTitle());
        }
      });
    });
  }

  private List<CakeClientResponse> convertCakeStringToList(String cakeString) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.readValue(cakeString, new TypeReference<List<CakeClientResponse>>() {
      });

    } catch (JsonProcessingException e) {
      log.error("Error parsing string: " + cakeString);
      return Collections.emptyList();
    }
  }

  public Cake addCake(CakeRequest cakeRequest) {
    long count = cakeRepository.countByTitle(cakeRequest.getTitle());

    if (count != 0) {
      throw new InvalidCakeException(String.format("Cake with title: %s already exists", cakeRequest.getTitle()));
    }

    Cake cake = new Cake();
    cake.setTitle(cakeRequest.getTitle());
    cake.setDescription(cakeRequest.getDescription());
    cake.setImage(cakeRequest.getImage());

    return cakeRepository.save(cake);
  }

  public Cake getCake(Long cakeId) {
    return cakeRepository.findById(cakeId).orElseThrow(() -> new CakeNotFoundException(String.format("Cake with id: %s not found", cakeId)));
  }


  public Page<Cake> getAllCakes(Pageable pageable) {
    return cakeRepository.findAll(pageable);
  }
}
