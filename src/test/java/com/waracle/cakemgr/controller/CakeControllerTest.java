package com.waracle.cakemgr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.dto.CakeRequest;
import com.waracle.cakemgr.model.Cake;
import com.waracle.cakemgr.service.CakeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CakeController.class)
public class CakeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CakeService cakeService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void givenNoCakeInDbShouldReturnEmptyList() throws Exception {
    given(cakeService.getAllCakes(any())).willReturn(Page.empty());

    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data", Matchers.is(Collections.emptyList())));
  }

  @Test
  public void givenPaginationShouldEvaluateParameters() throws Exception {
    given(cakeService.getAllCakes(any())).willReturn(Page.empty());

    mockMvc.perform(get("/")
            .param("size", "2"))
            .andExpect(status().isOk());

    ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
    verify(cakeService).getAllCakes(pageableCaptor.capture());
    PageRequest pageable = (PageRequest) pageableCaptor.getValue();

    assertEquals(2, pageable.getPageSize());
  }

  @Test
  public void givenMissingParamShouldThrowException() throws Exception {
    CakeRequest cakeRequest = new CakeRequest("Sponge Cake", "", "");

    mockMvc.perform(post("/cakes")
            .content(objectMapper.writeValueAsString(cakeRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

  }

  @Test
  public void givenCorrectRequestShouldReturn201() throws Exception {
    CakeRequest cakeRequest = new CakeRequest("Sponge Cake", "Test Cake", "http://image");

    Cake cake = new Cake();
    cake.setTitle(cakeRequest.getTitle());

    given(cakeService.addCake(cakeRequest)).willReturn(cake);

    mockMvc.perform(post("/cakes")
            .content(objectMapper.writeValueAsString(cakeRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.title", is("Sponge Cake")));
  }

  @Test
  public void givenCakeIdThatExistsShouldReturnCake() throws Exception {
    given(cakeService.getCake(5L)).willReturn(any());

    mockMvc.perform(get("/cakes/5")).andExpect(status().isOk());
  }

}
