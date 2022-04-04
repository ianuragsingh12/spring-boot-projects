package com.example.ud.service;

import com.example.ud.entity.FileData;
import com.example.ud.model.FileModelResponse;
import com.example.ud.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileDataServiceImpl implements FileDataService {

    @Autowired
    private FileDataRepository fileDataRepository;

    @Override
    public FileData saveFile(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence "
                        + fileName);
            }

            FileData data = new FileData();
            data.setFileName(fileName);
            data.setContentType(file.getContentType());
            data.setFileSizeBytes(file.getSize());
            data.setFileData(file.getBytes());
            return fileDataRepository.save(data);

        } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
        }
    }

    @Override
    public FileData getFile(String fileId) throws Exception {
        return fileDataRepository.findById(fileId)
                .orElseThrow(() -> new Exception("File not found with fileId: " + fileId));
    }
}
