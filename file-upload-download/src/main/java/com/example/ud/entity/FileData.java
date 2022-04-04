package com.example.ud.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Data
public class FileData implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "uuid2")
    @Column(name = "file_id")
    private String fileId;
    //@Column(name = "file_name")
    private String fileName;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "file_size")
    private long fileSizeBytes;
    @Lob
    @Column(name = "file_data")
    private byte[] fileData;
}
