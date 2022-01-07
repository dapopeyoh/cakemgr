package com.waracle.cakemgr.repository;

import com.waracle.cakemgr.model.Cake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CakeRepository extends JpaRepository<Cake, Long> {
  long countByTitle(String title);
}
