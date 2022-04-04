package com.example.ud.service;

import com.example.ud.entity.FileData;
import org.springframework.web.multipart.MultipartFile;

public interface FileDataService {
    FileData saveFile(MultipartFile file) throws Exception;

    FileData getFile(String fileId) throws Exception;
}
