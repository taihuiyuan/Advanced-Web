package com.example.gridfsdemo.controller;

import com.example.gridfsdemo.service.LargeFileService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
public class LargeFileController {

    @Resource
    LargeFileService largeFileService;

    @PostMapping("/uploadLargeFile")
    public String uploadLargeFile(@RequestParam("fileData") MultipartFile fileData) {
        return largeFileService.uploadLargeFile(fileData.getOriginalFilename(), fileData);
    }

    @GetMapping("/downloadLargeFile")
    public ResponseEntity<?> downloadLargeFile(@RequestParam("fileID") String fileID) {
        return largeFileService.downloadLargeFile(fileID);
    }

}
