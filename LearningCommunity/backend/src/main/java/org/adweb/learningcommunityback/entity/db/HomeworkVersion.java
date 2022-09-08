package org.adweb.learningcommunityback.entity.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "homework_version")
public class HomeworkVersion {
    @Id
    private String homeworkVersionID;

    @Indexed(name = "homeworkID_index")
    private String homeworkID;

    private String content;

    @Indexed(name = "fromUsername_index")
    private String fromUsername;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;

    @Indexed(name = "expire_time_index", expireAfterSeconds = 0)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireTime;

    public HomeworkVersion(String homeworkID, String content, String fromUsername, Date createdTime, Date expireTime) {
        this.homeworkID = homeworkID;
        this.content = content;
        this.fromUsername = fromUsername;
        this.createdTime = createdTime;
        this.expireTime = expireTime;
    }
}
