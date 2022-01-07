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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CakeServiceTest {

  @Mock
  private CakeRepository cakeRepository;

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

}
