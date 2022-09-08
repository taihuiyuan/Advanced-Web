package org.adweb.learningcommunityback.entity.db;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "homework_notification")
public class HomeworkNotification {
    public static String NEW_HOMEWORK = "NEW_HOMEWORK";
    public static String HOMEWORK_UPDATED = "HOMEWORK_UPDATED";

    @Id
    private String homeworkNotificationID;

    @Indexed(name = "course_code_index")
    private String courseCode;

    @Indexed(name = "username_index")
    private String username;

    private String type;

    private String message;

    private String homeworkID;

    private String homeworkTitle;

    public HomeworkNotification(String courseCode, String username, String type, String message, String homeworkID, String homeworkTitle) {
        this.courseCode = courseCode;
        this.username = username;
        this.type = type;
        this.message = message;
        this.homeworkID = homeworkID;
        this.homeworkTitle = homeworkTitle;
    }
}
