package com.example.springwithmongodbexample.repository;

import com.example.springwithmongodbexample.entity.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstructorRepository extends MongoRepository<Instructor, String> {
    Page<Instructor> findAllByBuildingAndNameNot(String building, String name, Pageable pageable);
}
