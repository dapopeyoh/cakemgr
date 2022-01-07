package com.waracle.cakemgr.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<E> {
  private String message;
  private E data;
  private Pagination pagination;
}
