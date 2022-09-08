package org.adweb.learningcommunityback.business.homeworkmanagement.request;

import lombok.Data;

@Data
public class GiveScoreRequest {
    private String username;
    private String courseCode;
    private int score;
}
