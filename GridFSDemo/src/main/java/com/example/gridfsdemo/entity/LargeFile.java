package com.example.gridfsdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.InputStream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class LargeFile {

    @Id
    private String id;

    private String fileName;

    @Indexed(name = "gridFsId", unique = true)
    private String gridFsId;
}
