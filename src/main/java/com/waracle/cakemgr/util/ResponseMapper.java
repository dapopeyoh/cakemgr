package com.waracle.cakemgr.util;

import com.waracle.cakemgr.dto.ApiResponse;
import com.waracle.cakemgr.dto.Pagination;
import com.waracle.cakemgr.model.Cake;
import org.springframework.data.domain.Page;

import java.util.List;

public class ResponseMapper {

  public static ApiResponse<List<Cake>> mapPageToApiResponse(Page<Cake> cakes) {
    Pagination pagination = new Pagination();
    pagination.setCurrentPage(cakes.getNumber());
    pagination.setTotalPages(cakes.getTotalPages());
    pagination.setTotalItems(cakes.getTotalElements());

    return ApiResponse.<List<Cake>>builder()
            .data(cakes.getContent())
            .pagination(pagination)
            .build();
  }
}
