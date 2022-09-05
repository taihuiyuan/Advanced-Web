package com.example.springwithmongodbexample.service;

import com.example.springwithmongodbexample.entity.Instructor;
import com.example.springwithmongodbexample.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    public void addInstructor(Instructor instructor) {
        instructorRepository.save(instructor);
    }


    public List<Instructor> findInstructor(int pageSize, int pageNum) {

        //注意这里的PageRequest的pageNum是0-based的
        //return instructorRepository.findAll(PageRequest.of(pageNum, pageSize, Sort.by(Sort.Direction.DESC, "salary")));
        //return instructorRepository.findAll(PageRequest.of(pageNum, pageSize));

        //Page<Instructor> instructors = instructorRepository.findAllByBuildingAndNameNot("test", "test1", PageRequest.of(pageNum, pageSize));
        //return instructors.getContent();

        List<Instructor> results = instructorRepository.findAll();

        int count = results.size(); // 记录总数
        int pageCount; // 页数
        if (count % pageSize == 0) {
            pageCount = count / pageSize;
        } else {
            pageCount = count / pageSize + 1;
        }
        int fromIndex; // 开始索引
        int toIndex; // 结束索引
        if (pageCount != pageNum+1) {
            fromIndex = pageNum * pageSize;
            toIndex = fromIndex + pageSize;
            if(toIndex > count){
                fromIndex = (pageNum-1) * pageSize;
                toIndex = count;
            }
        } else {
            fromIndex = pageNum * pageSize;
            toIndex = count;
        }

        return results.subList(fromIndex, toIndex);
    }


}
