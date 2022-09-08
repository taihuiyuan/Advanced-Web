package org.adweb.learningcommunityback.business.homeworkmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDTO {
    private String username;
    private String realname;
    private Integer gottenPoint;
    private Integer maxPoint;
    private Integer finishedHomeworkCnt;
    private Integer totalHomeworkCnt;
    private Integer score;
}
