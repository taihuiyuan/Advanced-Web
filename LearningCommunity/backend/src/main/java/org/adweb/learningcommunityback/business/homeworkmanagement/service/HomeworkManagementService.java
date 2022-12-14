package org.adweb.learningcommunityback.business.homeworkmanagement.service;

import org.adweb.learningcommunityback.business.coursemanagement.dto.MemberDTO;
import org.adweb.learningcommunityback.business.homeworkmanagement.dto.*;
import org.adweb.learningcommunityback.business.homeworkmanagement.request.ReleaseHomeworkRequest;
import org.adweb.learningcommunityback.entity.db.*;
import org.adweb.learningcommunityback.entity.response.SimpleSuccessfulPostResponse;
import org.adweb.learningcommunityback.exception.AdWebBaseException;
import org.adweb.learningcommunityback.preensure.access.EnsureTeacherHasCourseAccess;
import org.adweb.learningcommunityback.preensure.access.EnsureUserHasCourseAccess;
import org.adweb.learningcommunityback.preensure.access.EnsureUserHasHomeworkAccess;
import org.adweb.learningcommunityback.preensure.access.EnsureUserHasSectionAccess;
import org.adweb.learningcommunityback.preensure.course.CourseCode;
import org.adweb.learningcommunityback.preensure.course.EnsureCourseExists;
import org.adweb.learningcommunityback.preensure.homework.EnsureHomeworkExists;
import org.adweb.learningcommunityback.preensure.homework.HomeworkID;
import org.adweb.learningcommunityback.preensure.section.EnsureSectionExists;
import org.adweb.learningcommunityback.preensure.section.SectionID;
import org.adweb.learningcommunityback.preensure.user.EnsureTeacherExists;
import org.adweb.learningcommunityback.preensure.user.EnsureUserExists;
import org.adweb.learningcommunityback.preensure.user.TeacherUsername;
import org.adweb.learningcommunityback.preensure.user.Username;
import org.adweb.learningcommunityback.repository.*;
import org.adweb.learningcommunityback.utils.EntityDTOMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class HomeworkManagementService {
    @Resource
    private HomeworkRepository homeworkRepository;

    @Resource
    private HomeworkVersionRepository homeworkVersionRepository;

    @Resource
    private HomeworkNotificationRepository homeworkNotificationRepository;

    @Resource
    private HomeworkSubmissionRecordRepository homeworkSubmissionRecordRepository;

    @Resource
    private Take2Repository take2Repository;

    @Resource
    private CourseRepository courseRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private EssayProblemFinishedRepository essayProblemFinishedRepository;

    @EnsureTeacherExists
    @EnsureCourseExists
    @EnsureTeacherHasCourseAccess
    public SimpleSuccessfulPostResponse releaseHomework(@TeacherUsername String username,
                                                        @CourseCode String courseCode,
                                                        String sectionID, String title, String details, int oldMax, int oldTTL) {
        Homework homework = new Homework(courseCode, sectionID, title, details, oldMax, oldTTL);
        homeworkRepository.save(homework);

        //????????????????????????????????????type???NEW_HOMEWORK
        List<User> members = getCourseMember(courseCode);
        String type = HomeworkNotification.NEW_HOMEWORK;
        String courseName = courseRepository.findByCourseCode(courseCode).getCourseName();
        String message = "???" + courseCode + ": " + courseName + "??????????????????" + title + "???";

        members.forEach(member -> {
            HomeworkNotification homeworkNotification = new HomeworkNotification(courseCode, member.getUsername(), type, message, homework.getHomeworkID(), title);
            homeworkNotificationRepository.save(homeworkNotification);
        });

        return new SimpleSuccessfulPostResponse("??????????????????");
    }

    public List<User> getCourseMember(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode);
        List<Take2> take2s = take2Repository.findAllByCourseID(course.getId());
        List<User> users = new ArrayList<>();

        take2s.forEach(take2 -> {
            User user = userRepository.findById(take2.getUserID()).orElse(null);
            if (user != null) {
                users.add(user);
            }
        });

        return users;
    }

    @EnsureUserExists
    @EnsureSectionExists
    @EnsureUserHasSectionAccess
    public List<HomeworkBriefDTO> getAllHomeworkBriefing(@Username String username, @SectionID String sectionID) {
        List<Homework> homeworks = homeworkRepository.findAllBySectionID(sectionID);
        return homeworks.stream().map(HomeworkBriefDTO::from).toList();
    }

    @EnsureUserExists
    @EnsureHomeworkExists
    @EnsureUserHasHomeworkAccess
    public Homework getHomeworkDetails(@Username String username, @HomeworkID String homeworkID) {
        return homeworkRepository.findByHomeworkID(homeworkID);
    }

    @EnsureUserExists
    @EnsureHomeworkExists
    @EnsureUserHasHomeworkAccess
    public AllVersionBriefDTO getAllVersionBriefing(@Username String username, @HomeworkID String homeworkID) {
        HomeworkVersion latestVersion = homeworkVersionRepository.findByHomeworkIDAndExpireTimeNull(homeworkID);

        List<HomeworkVersion> oldVersions = homeworkVersionRepository.findAllByHomeworkIDAndExpireTimeNotNull(homeworkID);

        //???old???????????????????????????
        oldVersions.sort((v1, v2) -> Long.compare(v2.getExpireTime().getTime(), v1.getExpireTime().getTime()));


        VersionBriefDTO latest = VersionBriefDTO.from(latestVersion);
        List<VersionBriefDTO> old = oldVersions.stream()
                .map(VersionBriefDTO::from).toList();

        return new AllVersionBriefDTO(latest, old);
    }

    @EnsureUserExists
    public HomeworkVersion getVersionDetails(@Username String username, String homeworkVersionID) {
        return homeworkVersionRepository.findByHomeworkVersionID(homeworkVersionID);
    }

    @EnsureUserExists
    @EnsureHomeworkExists
    @EnsureUserHasHomeworkAccess
    public synchronized SimpleSuccessfulPostResponse editHomework(@Username String username,
                                                                  @HomeworkID String homeworkID,
                                                                  String content) {
        /*????????????
???????????????T?????????????????????homeworkID???123???homework
1. ????????????homeworkID???123???????????????????????????????????????????????????????????????
2. ????????????homeworkID????????????????????????HomeworkVersion???expireTime??????null??????
3. ???????????????HomeworkVersion??????????????????oldMax???????????????expireTime?????????????????????HomeworkVersion
4. ????????????homeworkID????????????T???????????????????????????HomeworkVersion???expireTime???null?????????????????????expireTime???expireTime=T+oldTTL??????????????????version???T???????????????????????????????????????version???T??????????????????oldTTL???????????????????????????
5. ??????????????????HomeworkVersion???expireTime???null????????????????????????
?????????????????????homework?????????????????????expireTime=null??????????????????????????????????????????

6. ????????????????????????????????????type???HOMEWORK_UPDATED
         */
        Homework homework = homeworkRepository.findByHomeworkID(homeworkID);

        List<HomeworkVersion> homeworkVersions = homeworkVersionRepository.findAllByHomeworkIDAndExpireTimeNotNull(homeworkID);
        Integer oldMax = homework.getOldMax();
        int index = 0;
        //??????version??????????????????oldMax
        if (homeworkVersions.size() == oldMax) {
            Date closetExpireTime = homeworkVersions.get(0).getExpireTime();
            int i = 0;
            for (HomeworkVersion homeworkVersion : homeworkVersions) {
                //????????????????????????closetExpireTime?????????version
                if (homeworkVersion.getExpireTime().before(closetExpireTime)) {
                    index = i;
                    closetExpireTime = homeworkVersion.getExpireTime();
                }
                i++;
            }
            HomeworkVersion homeworkVersion = homeworkVersions.get(index);
            homeworkVersionRepository.delete(homeworkVersion);
        }

        //???????????????????????????expireTime
        HomeworkVersion newestHV = homeworkVersionRepository.findByHomeworkIDAndExpireTimeNull(homeworkID);
        Integer oldTTL = homework.getOldTTL();
        Date date = new Date();
        Date afterDate = new Date(date.getTime() + oldTTL * 1000);
        if (newestHV != null) {
            newestHV.setExpireTime(afterDate);
            //???????????????"?????????"
            homeworkVersionRepository.save(newestHV);
        }

        //??????????????????
        HomeworkVersion newHomeworkVersion = new HomeworkVersion(homeworkID, content, username, date, null);
        homeworkVersionRepository.save(newHomeworkVersion);

        //????????????????????????
        HomeworkSubmissionRecord homeworkSubmissionRecord = homeworkSubmissionRecordRepository.findByUsernameAndCourseCodeAndHomeworkID(username, homework.getCourseCode(), homeworkID);
        if (homeworkSubmissionRecord == null) {
            homeworkSubmissionRecordRepository.save(new HomeworkSubmissionRecord(username, homework.getCourseCode(), homeworkID));
        }

        //????????????????????????????????????type???HOMEWORK_UPDATED
        String courseCode = homework.getCourseCode();
        List<User> members = getCourseMember(courseCode);
        String type = HomeworkNotification.HOMEWORK_UPDATED;
        String courseName = courseRepository.findByCourseCode(courseCode).getCourseName();
        String message = "???" + courseCode + ": " + courseName + "??????????????????" + homework.getTitle() + "???";
        members.forEach(member -> {
            HomeworkNotification homeworkNotification = new HomeworkNotification(courseCode, member.getUsername(), type, message, homeworkID, homework.getTitle());
            homeworkNotificationRepository.save(homeworkNotification);
        });

        return new SimpleSuccessfulPostResponse("??????????????????");
    }

    @EnsureUserExists
    @EnsureCourseExists
    @EnsureUserHasCourseAccess
    public List<HomeworkNotification> getAllHomeworkNotifications(@Username String username,
                                                                  @CourseCode String courseCode) {
        return homeworkNotificationRepository.findAllByCourseCodeAndUsername(courseCode, username);
    }

    @EnsureUserExists
    public SimpleSuccessfulPostResponse dismissNotification(@Username String username, String homeworkNotificationID) {
        HomeworkNotification homeworkNotification = homeworkNotificationRepository.findByHomeworkNotificationID(homeworkNotificationID);
        homeworkNotificationRepository.delete(homeworkNotification);
        return new SimpleSuccessfulPostResponse("?????????????????????");
    }

    @EnsureUserExists
    public List<MyHomeworkDTO> getMyHomework(@Username String username) {
        //????????????????????????
        List<Take2> takes = take2Repository.findAllByUserID(userRepository.findByUsername(username).getId());

        List<MyHomeworkDTO> result = new ArrayList<>();
        //???????????????????????????????????????
        takes.forEach(take2 -> {
            Course course = courseRepository.findById(take2.getCourseID()).orElse(null);
            if (course == null) {
                throw new AdWebBaseException(IllegalArgumentException.class, "COURSE_NOT_EXISTS", "???????????????");
            }
            List<Homework> homeworks = homeworkRepository.findAllByCourseCode(course.getCourseCode());

            //????????????????????????????????????HomeworkSubmissionRecord????????????????????????
            homeworks.forEach(homework -> {
                HomeworkSubmissionRecord homeworkSubmissionRecord = homeworkSubmissionRecordRepository.findByUsernameAndCourseCodeAndHomeworkID(username, course.getCourseCode(), homework.getHomeworkID());
                MyHomeworkDTO myHomeworkDTO;
                if (homeworkSubmissionRecord == null) {//?????????
                    myHomeworkDTO = new MyHomeworkDTO(homework.getHomeworkID(), homework.getTitle(), course.getCourseCode(), course.getCourseName(), false);
                } else {
                    myHomeworkDTO = new MyHomeworkDTO(homework.getHomeworkID(), homework.getTitle(), course.getCourseCode(), course.getCourseName(), true);
                }
                result.add(myHomeworkDTO);
            });
        });

        //?????????????????????????????????
        result.sort(new Comparator<MyHomeworkDTO>() {
            @Override
            public int compare(MyHomeworkDTO o1, MyHomeworkDTO o2) {
                boolean homework1 = o1.isFinished();
                boolean homework2 = o2.isFinished();
                //????????????????????????,????????????1,???true,????????????
                //????????????0,???false,???????????????
                if (homework1 ^ homework2) {
                    return homework1 ? 1 : -1;
                } else {
                    return 0;
                }
            }
        });
        return result;
    }

    @EnsureUserExists
    @EnsureCourseExists
    @EnsureUserHasCourseAccess
    public SimpleSuccessfulPostResponse giveScore(@Username String teacherUsername, @Username String username, @CourseCode String courseCode, int score) {
        //??????userID???courseID???????????????take2
        User user = userRepository.findByUsername(username);
        String userId = user.getId();
        Course course = courseRepository.findByCourseCode(courseCode);
        String courseId = course.getId();
        Take2 take2 = take2Repository.findByUserIDAndCourseID(userId, courseId);

        //??????score
        if (score < 0 || score > 100) {
            throw new AdWebBaseException(IllegalArgumentException.class, "SCORE_ERROR", "???????????????");
        } else {
            take2.setScore(score);
            take2Repository.save(take2);
        }

        take2Repository.save(take2);

        return new SimpleSuccessfulPostResponse("????????????");
    }

    @EnsureUserExists
    @EnsureCourseExists
    @EnsureUserHasCourseAccess
    public List<ScoreDTO> getScores(@Username String username, @CourseCode String courseCode) {
        //?????????????????????????????????
        Course course = courseRepository.findByCourseCode(courseCode);
        String courseId = course.getId();
        List<Take2> take2s = take2Repository.findAllByCourseID(courseId);
        List<String> studentNames = new ArrayList<>();
        List<ScoreDTO> results = new ArrayList<>();
        for (Take2 take2 : take2s) {
            String userId = take2.getUserID();
            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.getRole().equals(User.ROLE_STUDENT)) {
                    studentNames.add(user.getUsername());
                }
            }
        }

        List<Homework> homeworks = homeworkRepository.findAllByCourseCode(courseCode);
        int totalHomeworkCnt = homeworks.size();

        //??????????????????
        //1????????????????????????????????????/?????????EssayProblemFinished
        for (String studentName : studentNames) {
            int gottenPoint = 0;
            int maxPoint = 0;
            List<EssayProblemFinished> essayProblemFinisheds = essayProblemFinishedRepository.findAllByCourseCodeAndAnswerUsername(courseCode, studentName);
            for (EssayProblemFinished essayProblemFinished : essayProblemFinisheds) {
                gottenPoint += essayProblemFinished.getGottenPoint();
                maxPoint += essayProblemFinished.getMaxPoint();
            }

            //2???????????????????????????????????????/?????????HomeworkSubmissionRecord/Homework
            List<HomeworkSubmissionRecord> homeworkSubmissionRecords = new ArrayList<>();
            for (Homework homework : homeworks) {
                String homeworkID = homework.getHomeworkID();
                HomeworkSubmissionRecord homeworkSubmissionRecord = homeworkSubmissionRecordRepository.findByUsernameAndHomeworkID(studentName, homeworkID);
                if (homeworkSubmissionRecord != null) {
                    homeworkSubmissionRecords.add(homeworkSubmissionRecord);
                }
            }
            int finishedHomeworkCnt = homeworkSubmissionRecords.size();


            //3?????????????????????
            User user = userRepository.findByUsername(studentName);
            String userId = user.getId();
            String realname = user.getRealname();
            Take2 take2 = take2Repository.findByUserIDAndCourseID(userId, courseId);
            Integer score = take2.getScore();

            ScoreDTO scoreDTO = new ScoreDTO(studentName, realname, gottenPoint, maxPoint, finishedHomeworkCnt, totalHomeworkCnt, score);
            results.add(scoreDTO);
        }

        return results;
    }
}
