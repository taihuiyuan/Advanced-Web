package com.example.gridfsdemo.service;

import com.example.gridfsdemo.entity.SmallFile;
import com.example.gridfsdemo.repository.SmallFileRepository;
import lombok.SneakyThrows;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class SmallFileService {

    @Resource
    private SmallFileRepository smallFileRepository;

    @SneakyThrows
    public String uploadSmallFile(String fileName, MultipartFile fileData) {
        SmallFile smallFile = new SmallFile();
        smallFile.setFileName(fileName);
        smallFile.setFileData(new Binary(BsonBinarySubType.BINARY, fileData.getBytes()));

        smallFileRepository.save(smallFile);

        return smallFile.getId();
    }

    @SneakyThrows
    public ResponseEntity<?> downloadSmallFile(String fileID){
        SmallFile smallFile = smallFileRepository.findById(fileID).orElse(null);
        if (smallFile == null) {
            return ResponseEntity.notFound().build();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(smallFile.getFileData().length());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", URLEncoder.encode(smallFile.getFileName(), StandardCharsets.UTF_8)));

        InputStreamResource resource
                = new InputStreamResource(new ByteArrayInputStream(smallFile.getFileData().getData()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);

    }
}
