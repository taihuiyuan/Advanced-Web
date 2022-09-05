package com.example.gridfsdemo.controller;

import com.example.gridfsdemo.service.SmallFileService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class SmallFileController {

    @Resource
    private SmallFileService smallFileService;

    @PostMapping("/uploadSmallFile")
    public String uploadSmallFile(
            @RequestParam(value = "fileData") MultipartFile fileData) {
        return smallFileService.uploadSmallFile(fileData.getOriginalFilename(), fileData);
    }

    @GetMapping("/downloadSmallFile")
    public ResponseEntity<?> downloadSmallFile(@RequestParam(value = "fileID") String fileID) {
        return smallFileService.downloadSmallFile(fileID);
    }


}
