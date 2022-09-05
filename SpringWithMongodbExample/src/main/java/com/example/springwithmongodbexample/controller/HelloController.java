package com.example.springwithmongodbexample.controller;

import com.example.springwithmongodbexample.entity.Instructor;
import com.example.springwithmongodbexample.service.InstructorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloController {
    @Resource
    private InstructorService instructorService;

    @GetMapping("/hello")
    public String hello(){
        return "This is hello world from spring boot";
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") String name, @RequestParam("salary") int salary, @RequestParam("building") String building){
        Instructor instructor = new Instructor();
        instructor.setName(name);
        instructor.setSalary(salary);
        instructor.setBuilding(building);
        instructorService.addInstructor(instructor);
        return "success";
    }

    @GetMapping("/find")
    public List<Instructor> find(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum){
        return instructorService.findInstructor(pageSize, pageNum);
    }
}
