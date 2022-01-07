package com.waracle.cakemgr.controller;

import com.waracle.cakemgr.dto.ApiResponse;
import com.waracle.cakemgr.dto.CakeRequest;
import com.waracle.cakemgr.model.Cake;
import com.waracle.cakemgr.service.CakeService;
import com.waracle.cakemgr.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CakeController {

  private final CakeService cakeService;

  @PostMapping("/cakes")
  public ResponseEntity<ApiResponse<Cake>> addCake(@RequestBody @Valid CakeRequest cakeRequest) {
    Cake cake = cakeService.addCake(cakeRequest);
    ApiResponse<Cake> response = ApiResponse.<Cake>builder()
            .message("Request Successful")
            .data(cake)
            .build();

    URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(cake.getId()).toUri();

    return ResponseEntity.created(location).body(response);
  }

  @GetMapping("/cakes/{id}")
  public ResponseEntity<ApiResponse<Cake>> getCake(@PathVariable Long id) {
    Cake cake = cakeService.getCake(id);

    ApiResponse<Cake> apiResponse = ApiResponse.<Cake>builder()
            .message("Request Successful")
            .data(cake)
            .build();

    return ResponseEntity.ok(apiResponse);
  }

  @GetMapping({"", "/cakes"})
  public ResponseEntity<ApiResponse<List<Cake>>> getAllCakes(Pageable pageable) {
    Page<Cake> cakes = cakeService.getAllCakes(pageable);

    ApiResponse<List<Cake>> response = ResponseMapper.mapPageToApiResponse(cakes);
    response.setMessage("Request Successful");

    return ResponseEntity.ok(response);
  }

//  @GetMapping({"/", "/cakes"})
//  public ResponseEntity<ApiResponse<List<Cake>>> getAllCakes(Pageable pageable) {
//    Page<Cake> cakes = cakeService.getAllCakes(pageable);
//
//    ApiResponse<List<Cake>> response = ResponseMapper.mapPageToApiResponse(cakes);
//    response.setMessage("Request Successful");
//
//    return ResponseEntity.ok(response);
//  }
}
