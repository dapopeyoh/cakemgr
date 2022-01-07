package com.waracle.cakemgr.dto;

import lombok.Data;

@Data
public class Pagination {
  private int currentPage;
  private int totalPages;
  private long totalItems;
}
