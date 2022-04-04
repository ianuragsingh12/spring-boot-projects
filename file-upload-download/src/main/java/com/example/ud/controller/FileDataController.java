package com.example.ud.controller;

import com.example.ud.entity.FileData;
import com.example.ud.model.FileModelResponse;
import com.example.ud.service.FileDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Slf4j
@RequestMapping("/api/files")
public class FileDataController {
    @Autowired
    private FileDataService fileDataService;

    @PostMapping("/upload")
    public FileModelResponse uploadFIle(@RequestParam("file") MultipartFile file) throws Exception {

        String downloadURl = "";
        FileData fileData = fileDataService.saveFile(file);

        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/download/")
                .path(fileData.getFileId())
                .toUriString();

        return new FileModelResponse(fileData.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFIle(@PathVariable String fileId) throws Exception {
        FileData fileData = fileDataService.getFile(fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; fileName=\"" + fileData.getFileName() + "\"")
                .contentType(MediaType.valueOf(fileData.getContentType()))
                .body(new ByteArrayResource(fileData.getFileData()));
    }
}
