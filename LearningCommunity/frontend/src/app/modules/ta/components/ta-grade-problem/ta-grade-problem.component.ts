import {Component, OnInit, Input} from '@angular/core';
import {
  EssayProblemToBeTeacherTAGradedDTO
} from "../../../../services/gradeProblem/dto/essayProblemToBeTeacherTAGraded";
import {GradeProblemService} from "../../../../services/gradeProblem/grade-problem.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {NzTableQueryParams} from "ng-zorro-antd/table";


@Component({
  selector: 'app-ta-grade-problem',
  templateUrl: './ta-grade-problem.component.html',
  styleUrls: ['./ta-grade-problem.component.css']
})
export class TaGradeProblemComponent implements OnInit {

  @Input()
  public courseCode: string = ''

  public loading: boolean = false;

  public pageNum: number = 1
  public pageSize: number = 10
  public totalPage: number = 1
  public totalElement: number = 0

  public essayProblemToBeTeacherTAGradedList: EssayProblemToBeTeacherTAGradedDTO [] = []
  public gradeList: number[] = []

  constructor(private gradeProblemService: GradeProblemService,
              private message: NzMessageService) {
  }

  ngOnInit(): void {
    this.getEssayProblemsToBeTeacherTAGraded()
  }


  public hasProblemToBeGraded(): boolean {
    return this.essayProblemToBeTeacherTAGradedList.length > 0
  }

  public getEssayProblemsToBeTeacherTAGraded() {
    this.loading = true
    this.gradeProblemService.essayProblemsToBeTeacherTAGraded(
      this.courseCode,
      this.pageNum,
      this.pageSize,
      httpResponse => {
        this.loading = false;
        this.totalPage = httpResponse.body?.totalPage as number
        this.totalElement = httpResponse.body?.totalElement as number
        this.essayProblemToBeTeacherTAGradedList = httpResponse.body?.data as EssayProblemToBeTeacherTAGradedDTO[]
      },
      httpErrorResponse => {
        this.loading = false
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  public onQueryParamsChange(params: NzTableQueryParams) {
    this.gradeList = []
    const {pageSize, pageIndex, sort, filter} = params;
    this.pageSize = pageSize;
    this.pageNum = pageIndex;
    this.getEssayProblemsToBeTeacherTAGraded();
  }

  public submit(i: number) {
    if (!this.gradeList[i]) {
      this.message.error("分数不能为空");
      return
    }

    if (this.gradeList[i] > this.essayProblemToBeTeacherTAGradedList[i].maxPoint) {
      this.message.error("评分不能超过最大分数");
      return
    }

    let grade: number = this.gradeList[i];

    this.gradeProblemService.TeacherTAGradeEssayProblem(
      this.essayProblemToBeTeacherTAGradedList[i].essayProblemToBeTeacherTAGradedID,
      grade,
      httpResponse => {
        this.message.success(httpResponse.body?.message as string)
        this.essayProblemToBeTeacherTAGradedList.splice(i, 1)
        this.gradeList.splice(i, 1)
        this.getEssayProblemsToBeTeacherTAGraded()
      },
      httpErrorResponse => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

}