import {Component, OnInit, Input} from '@angular/core';
import {ScoresService} from "../../../../services/score/scores.service";
import {NzMessageService} from "ng-zorro-antd/message";
// @ts-ignore
import {ScoreDTO} from "../../../../services/score/dto/ScoreDTO";


@Component({
  selector: 'app-scores',
  templateUrl: './scores.component.html',
  styleUrls: ['./scores.component.css']
})
export class ScoresComponent implements OnInit {

  @Input()
  public courseCode: string = ''
  @Input()
  public isTeacher: boolean = false

  public scores: ScoreDTO[] = []
  public teacherGrade: number[] = []


  constructor(private scoreService: ScoresService,
              private message: NzMessageService) {
  }

  ngOnInit(): void {
    this.getScore()
  }

  public getScore() {
    this.scoreService.getScores(this.courseCode,
      httpResponse => {
        this.scores = httpResponse.body as unknown as ScoreDTO[]
        for (let i = 0; i < this.scores.length; i++) {
          this.teacherGrade[i] = this.scores[i].score ? this.scores[i].score : 0
        }
      },
      httpErrorResponse => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  public submit(i: number) {
    if (this.teacherGrade[i] === null || this.teacherGrade[i] === undefined) {
      this.message.error("分数不能为空");
      return
    }
    if (this.teacherGrade[i] > 100 || this.teacherGrade[i] < 0) {
      this.message.error("分数区间应该为0-100");
      return
    }

    this.scoreService.giveScore(
      this.scores[i].username,
      this.courseCode,
      this.teacherGrade[i],
      httpResponse => {
        this.message.success(httpResponse.body?.message as string)
        this.getScore()
      },
      httpErrorResponse => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }


}
