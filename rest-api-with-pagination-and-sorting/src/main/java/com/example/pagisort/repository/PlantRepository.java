package com.example.pagisort.repository;

import com.example.pagisort.entity.Plant;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends PagingAndSortingRepository<Plant, Long> {
}
