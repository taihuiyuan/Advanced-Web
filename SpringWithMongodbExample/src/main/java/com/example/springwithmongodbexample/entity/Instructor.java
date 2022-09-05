package com.example.springwithmongodbexample.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "instructor")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Instructor {
    @Id
    private String id;
    @Indexed(name = "name_index",unique = true)
    private String name;
    private Integer salary;
    private String building;

}
