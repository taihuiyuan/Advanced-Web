package com.example.gridfsdemo.repository;

import com.example.gridfsdemo.entity.SmallFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmallFileRepository extends MongoRepository<SmallFile, String> {
}
