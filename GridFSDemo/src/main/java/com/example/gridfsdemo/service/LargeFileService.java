package com.example.gridfsdemo.service;

import com.example.gridfsdemo.entity.LargeFile;
import com.example.gridfsdemo.entity.SmallFile;
import com.example.gridfsdemo.repository.LargeFileRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.SneakyThrows;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class LargeFileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private LargeFileRepository largeFileRepository;

    @Autowired
    private GridFsOperations operations;

    @SneakyThrows
    public String uploadLargeFile(String fileName, MultipartFile fileData) {


        DBObject metadata = new BasicDBObject();
        ObjectId id = gridFsTemplate.store(
                fileData.getInputStream(),
                fileData.getOriginalFilename(),
                fileData.getContentType(),
                metadata
        );

        LargeFile largeFile = new LargeFile();
        largeFile.setFileName(fileName);
        largeFile.setGridFsId(id.toString());

        largeFileRepository.save(largeFile);

        return largeFile.getId();
    }


    @SneakyThrows
    public ResponseEntity<?> downloadLargeFile(String fileId) {
        LargeFile largeFile = largeFileRepository.findById(fileId).orElse(null);
        if (largeFile == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        String gridFsId = largeFile.getGridFsId();

        GridFSFile gridFsFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(gridFsId)));
        if (gridFsFile == null) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(gridFsFile.getLength());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment;filename=%s", URLEncoder.encode(largeFile.getFileName(), StandardCharsets.UTF_8)));

        InputStreamResource resource
                = new InputStreamResource(operations.getResource(gridFsFile).getInputStream());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(resource);


    }


}
