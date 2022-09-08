package org.adweb.learningcommunityback.repository;

import org.adweb.learningcommunityback.entity.db.HomeworkSubmissionRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkSubmissionRecordRepository extends MongoRepository<HomeworkSubmissionRecord, String> {
    public HomeworkSubmissionRecord findByUsernameAndCourseCodeAndHomeworkID(String username, String courseCode, String homeworkID);
    public HomeworkSubmissionRecord findByUsernameAndHomeworkID(String username, String homeworkID);
}
