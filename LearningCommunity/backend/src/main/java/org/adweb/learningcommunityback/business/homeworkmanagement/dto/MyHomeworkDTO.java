package org.adweb.learningcommunityback.business.homeworkmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyHomeworkDTO {
    private String homeworkID;
    private String title;
    private String courseCode;
    private String courseName;
    private boolean finished;
}
