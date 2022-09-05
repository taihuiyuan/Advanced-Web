package com.example.springwithmongodbexample.service;

import com.alibaba.fastjson.JSON;
import com.example.springwithmongodbexample.entity.Instructor;
import com.example.springwithmongodbexample.repository.InstructorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import javax.annotation.Resource;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootTest
class InstructorServiceTest {

    @Resource
    InstructorService instructorService;

    @Resource
    InstructorRepository instructorRepository;

    @Test
    void addInstructor() {
        Random random = new Random();
        final int CNT = 10000;
        for (int i = 0; i < CNT; i++) {
            Instructor instructor = new Instructor(null, UUID.randomUUID().toString(), random.nextInt(500000), "Building" + random.nextInt(100));
            instructorService.addInstructor(instructor);
        }
    }

    @Test
    void updateInstructor() {
    }

    @Test
    void getAllInstructors() {
        List<Instructor> instructorList = instructorRepository.findAll();
        instructorList.stream().map(JSON::toJSONString).forEach(System.out::println);
    }

    @Test
    void testFindInstructorPage() {
        List<Instructor> page = instructorService.findInstructor(10, 0);
        page.forEach(System.out::println);
    }
}
