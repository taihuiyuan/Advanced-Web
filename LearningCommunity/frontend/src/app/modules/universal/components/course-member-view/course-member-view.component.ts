import {Component, Input, OnInit} from '@angular/core';
import {CourseService} from "../../../../services/course/course.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {CourseMember} from "../../../../services/course/dtos/CourseMember";
import {NzMessageService} from "ng-zorro-antd/message";
import {SuccessfulPostResponse} from "../../../../reqrsp/SuccessfulPostResponse";
import {firstValueFrom} from "rxjs";
import {UserInfoService} from "../../../../services/user-info/user-info.service";

@Component({
  selector: 'app-course-member-view',
  templateUrl: './course-member-view.component.html',
  styleUrls: ['./course-member-view.component.css']
})
export class CourseMemberViewComponent implements OnInit {

  @Input()
  public courseCode: string | undefined

  @Input()
  public isTeacher: boolean | undefined

  public courseMembers: CourseMember[] = []

  public avatars: string[] = []


  /**
   * @deprecated 这个方法只能获取成员信息，不能获取他们的头像信息
   * 获取课程成员的函数
   */
  public getCourseMemberFunc = () => {
    this.courseService.getCourseMember(
      this.courseCode as string,
      (httpResponse: HttpResponse<CourseMember[]>) => {
        this.courseMembers = httpResponse.body as CourseMember[];
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  /**
   * 获取课程成员和成员头像的异步函数
   */
  public asyncLoadCourseMemberAndAvatar = async () => {
    let courseMembers: CourseMember[] = (await firstValueFrom(this.courseService.getCourseMemberObservable(this.courseCode as string))).body as CourseMember[]

    let avatars: string[] = []

    for (let i = 0; i < courseMembers.length; i++) {
      let usernameLocal = courseMembers[i].username;
      let avatarLocal = (await firstValueFrom(this.userInfoService.getOtherAvatarSrcObservable(usernameLocal))).body['avatar']
      avatars.push(avatarLocal)
    }

    this.courseMembers = courseMembers;
    this.avatars = avatars;

  }

  constructor(private courseService: CourseService,
              private message: NzMessageService,
              private userInfoService: UserInfoService) {
  }


  public removeCourseStudentTA(username: string) {
    this.courseService.removeCourseStudentTA(
      this.courseCode as string,
      username,
      (httpResponse: HttpResponse<SuccessfulPostResponse>) => {

        //弹出成功提示
        this.message.success(httpResponse.body?.message as string)

        //重新获取课程成员和头像
        this.asyncLoadCourseMemberAndAvatar().then()
          .catch(err => {
            this.message.error(JSON.stringify(err.error))
          })
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  reload(): void {
    this.asyncLoadCourseMemberAndAvatar()
      .then().catch(err => {
      this.message.error(JSON.stringify(err.error))
    })
  }


  ngOnInit(): void {
    this.asyncLoadCourseMemberAndAvatar()
      .then()
      .catch(err => {
        this.message.error(JSON.stringify(err.error))
      })
  }


}
