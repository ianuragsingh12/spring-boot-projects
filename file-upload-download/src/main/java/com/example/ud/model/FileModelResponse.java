package com.example.ud.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileModelResponse {
    private String fileName;
    private String downloadURL;
    private String fileType;
    private long fileSize;
}
