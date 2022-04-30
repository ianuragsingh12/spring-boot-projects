package com.example.pagisort.service;

import com.example.pagisort.model.PageSettings;
import com.example.pagisort.entity.Plant;
import com.example.pagisort.repository.PlantRepository;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PlantService {

    private final PlantRepository plantRepository;

    public PlantService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    public Page<Plant> getPlantPage(@NonNull PageSettings pageSetting) {

        Sort plantSort = pageSetting.buildSort();
        Pageable plantPage = PageRequest
                .of(pageSetting.getPage(), pageSetting.getElementPerPage(), plantSort);

        return plantRepository.findAll(plantPage);
    }
}
