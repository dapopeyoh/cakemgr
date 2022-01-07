package com.waracle.cakemgr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CakeRequest {
  @NotBlank(message = "title field is required")
  private String title;

  @NotBlank(message = "description field is required")
  private String description;

  @NotBlank(message = "image field is required")
  private String image;
}
