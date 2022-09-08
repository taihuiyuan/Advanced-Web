import {Component, OnInit, Input} from '@angular/core';
import {Router} from "@angular/router";
import {CourseService} from "../../../../services/course/course.service";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {NzMessageService} from "ng-zorro-antd/message";
import {SuccessfulPostResponse} from "../../../../reqrsp/SuccessfulPostResponse";
import {NzTableQueryParams} from "ng-zorro-antd/table";
import {CourseDTO} from 'src/app/services/course/dtos/CourseDTO';
import {CommunicationService} from "../../../../services/communication/communication.service";

@Component({
  selector: 'app-join-course',
  templateUrl: './join-course.component.html',
  styleUrls: ['./join-course.component.css']
})
export class JoinCourseComponent implements OnInit {
  public courseName: string = ""

  public pageNum: number = 1;
  public pageSize: number = 10;
  public totalPage: number = 1;
  public totalElement: number = 0;
  public courses: CourseDTO[] = []
  public loading: boolean = false;
  public myCourses: CourseDTO[] = []


  constructor(private router: Router,
              private courseService: CourseService,
              private message: NzMessageService,
              private communicationService: CommunicationService) {

  }

  ngOnInit(): void {
    this.getCourses();
  }

  public onQueryParamsChange(params: NzTableQueryParams) {
    const {pageSize, pageIndex, sort, filter} = params;
    this.pageSize = pageSize;
    this.pageNum = pageIndex;
    this.getMyCourses();
    this.getCourses();
  }

  public getMyCourses() {
    this.courseService.getMyRelatedCourses(
      httpResponse => {
        this.myCourses = httpResponse.body as Array<CourseDTO>
      },
      httpErrorResponse => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  /**
   * 加入课程
   */
  public joinCourse(courseCode: string) {
    this.courseService.joinCourse(
      courseCode,
      (httpResponse: HttpResponse<SuccessfulPostResponse>) => {
        //弹出成功提示
        this.message.success(httpResponse.body?.message as string)
        this.courseName = ""
        //通知我的课程信息已发生变化，需要重新获取
        this.communicationService.emitMyCourseChange();

        //获得我的课程
        this.getMyCourses()
      },
      (httpErrorResponse: HttpErrorResponse) => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  public getCourses() {
    this.courseService.getCourse(this.courseName, this.pageNum, this.pageSize,
      httpResponse => {
        this.loading = false;
        this.totalPage = httpResponse.body?.totalPage as number;
        this.totalElement = httpResponse.body?.totalElement as number;
        this.courses = httpResponse.body?.data as CourseDTO[];
      },
      httpErrorResponse => {
        this.loading = false;
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  public hasCourse(courseCode: string) {
    for (let course of this.myCourses) {
      if (course.courseCode == courseCode) {
        return true
      }
    }
    return false;
  }


}
