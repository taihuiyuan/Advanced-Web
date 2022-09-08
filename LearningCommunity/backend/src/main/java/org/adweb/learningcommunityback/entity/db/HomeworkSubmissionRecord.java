package org.adweb.learningcommunityback.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@CompoundIndexes({
        @CompoundIndex(
                name = "username_homeworkID_index",
                def = "{username:1, homeworkID:1}",
                unique = true)
})
@Document(collection = "homework_submission_record")
public class HomeworkSubmissionRecord {
    @Id
    private String homeworkSubmissionRecordID;

    private String username;

    private String courseCode;

    private String homeworkID;

    public HomeworkSubmissionRecord(String username, String courseCode, String homeworkID){
        this.username = username;
        this.courseCode = courseCode;
        this.homeworkID = homeworkID;
    }
}
