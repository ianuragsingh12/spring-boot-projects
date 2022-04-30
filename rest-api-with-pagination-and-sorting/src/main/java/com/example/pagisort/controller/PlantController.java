package com.example.pagisort.controller;

import com.example.pagisort.mapper.PageToPageDTOMapper;
import com.example.pagisort.model.PageDTO;
import com.example.pagisort.model.PageSettings;
import com.example.pagisort.entity.Plant;
import com.example.pagisort.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController()
@RequestMapping("/plant")
public class PlantController {

    private final PlantService plantService;

    private final PageToPageDTOMapper<Plant> pageToPageDTOMapper;

    public PlantController(PlantService plantService, PageToPageDTOMapper<Plant> pageToPageDTOMapper) {
        this.plantService = plantService;
        this.pageToPageDTOMapper = pageToPageDTOMapper;
    }

    @GetMapping("/page")
    public PageDTO<Plant> getPlantPage(PageSettings pageSettings) {

        log.info("Request for plant page received with data : " + pageSettings);

        return pageToPageDTOMapper.pageToPageDTO(plantService.getPlantPage(pageSettings));
    }
}
