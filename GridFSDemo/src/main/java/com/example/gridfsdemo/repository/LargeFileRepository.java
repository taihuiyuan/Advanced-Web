package com.example.gridfsdemo.repository;


import com.example.gridfsdemo.entity.LargeFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LargeFileRepository extends MongoRepository<LargeFile, String> {
}
