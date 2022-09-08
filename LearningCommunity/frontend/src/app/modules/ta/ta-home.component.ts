import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CommonService} from "../../services/common/common.service";
import {CourseDTO} from "../../services/course/dtos/CourseDTO";
import {CourseService} from "../../services/course/course.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {NzMessageService} from "ng-zorro-antd/message";
import {LoginRequiredComponent} from "../LoginRequiredComponent";
import {UserInfoService} from "../../services/user-info/user-info.service";

@Component({
  selector: 'app-ta-home',
  templateUrl: './ta-home.component.html',
  styleUrls: ['./ta-home.component.css']
})
export class TAHomeComponent extends LoginRequiredComponent implements OnInit {
  public myRelatedCourses: Array<CourseDTO> = []

  //用户名和头像
  public username: string = this.commonService.getUsernameByJwt()
  public myAvatarSrc = ''

  constructor(public router: Router,
              public commonService: CommonService,
              private courseService: CourseService,
              private userInfoService: UserInfoService,
              private message: NzMessageService) {
    super(router, commonService)
  }

  ngOnInit(): void {
    this.getMyRelatedCourses();

    this.userInfoService
      .getOtherAvatarSrcObservable(this.commonService.getUsernameByJwt())
      .subscribe({
        next: httpResponse => {
          this.myAvatarSrc = httpResponse.body['avatar']
        }
      })
  }

  /**
   * 在页面开始加载时，就尝试获取自己的课程
   */
  public getMyRelatedCourses() {
    this.courseService.getMyRelatedCourses(
      (httpResponse: HttpResponse<Array<CourseDTO>>) => {
        this.myRelatedCourses = httpResponse.body as Array<CourseDTO>;
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.message.info(JSON.stringify(httpErrorResponse))
      }
    )
  }


  public logout(): void {
    this.commonService.deleteJwt();
    this.router.navigate(['universal', 'userLogin'])
  }

}
