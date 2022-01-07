package com.waracle.cakemgr.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cake")
@Data
public class Cake {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "title field is required")
  private String title;

  @NotBlank(message = "description field is required")
  private String description;

  @NotBlank(message = "image field is required")
  private String image;

}
