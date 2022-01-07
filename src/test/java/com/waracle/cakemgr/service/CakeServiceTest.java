package com.waracle.cakemgr.service;

import com.waracle.cakemgr.dto.CakeRequest;
import com.waracle.cakemgr.exception.InvalidCakeException;
import com.waracle.cakemgr.model.Cake;
import com.waracle.cakemgr.repository.CakeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CakeServiceTest {

  @Mock
  private CakeRepository cakeRepository;
  @Mock
  private WebClient webClient;
  @Mock
  private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
  @Mock
  private WebClient.ResponseSpec responseMock;
  @Mock
  private ApplicationReadyEvent event;

  @InjectMocks
  private CakeService cakeService;

  @Test
  public void givenCakeTitleExistsShouldThrowException() {
    CakeRequest cakeRequest = new CakeRequest("Sponge Cake", "Test Cake", "http://image");

    given(cakeRepository.countByTitle(cakeRequest.getTitle())).willReturn(1L);

    InvalidCakeException exception = assertThrows(InvalidCakeException.class, () -> {
      cakeService.addCake(cakeRequest);
    });

    String expectedMessage = "already exists";
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

  }

  @Test
  public void givenValidCakeRequestShouldReturnCake() {
    CakeRequest cakeRequest = new CakeRequest("Sponge Cake", "Test Cake", "http://image");

    given(cakeRepository.countByTitle(cakeRequest.getTitle())).willReturn(0L);
    given(cakeRepository.save(any())).willReturn(new Cake());

    Cake cake = cakeService.addCake(cakeRequest);

    verify(cakeRepository).save(any());
    assertNotNull(cake);
  }

  @Test
  public void givenValidCakeListShouldPersist() {
    ReflectionTestUtils.setField(cakeService, "cakeUrl", "api");

    String json = "[{\"title\":\"Lemon cheesecake\",\"desc\":\"A cheesecake made of lemon\",\"image\":\"https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg\"},{\"title\":\"victoria sponge\",\"desc\":\"sponge with jam\",\"image\":\"http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg\"}]";

    given(webClient.get()).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.uri("api")).willReturn(requestHeadersUriMock);
    given(requestHeadersUriMock.retrieve()).willReturn(responseMock);
    given(responseMock.bodyToMono(String.class)).willReturn(Mono.just(json));

    cakeService.init(event);

    verify(cakeRepository, times(2)).save(any());
  }

}
