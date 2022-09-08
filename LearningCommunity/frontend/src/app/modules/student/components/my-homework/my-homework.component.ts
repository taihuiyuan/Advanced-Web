import { Component, OnInit } from '@angular/core';
import{MyHomework} from "../../../../services/homework/dtos/MyHomework";
import {NzMessageService} from "ng-zorro-antd/message";
import {HomeworkService} from "../../../../services/homework/homework.service";

@Component({
  selector: 'app-my-homework',
  templateUrl: './my-homework.component.html',
  styleUrls: ['./my-homework.component.css']
})
export class MyHomeworkComponent implements OnInit {
  public myHomework:MyHomework[] = []
  public finished:string[] = []
  constructor(private homeworkService: HomeworkService,
              private message: NzMessageService) { }

  ngOnInit(): void {
    this.getMyHomework()
  }

  public getMyHomework(){
    this.homeworkService.getMyHomeWork(
      httpResponse => {
        this.myHomework = httpResponse.body as unknown as MyHomework[]
        for(let i = 0 ; i < this.myHomework.length; i++){
          if(this.myHomework[i].finished){
            this.finished[i] = "是"
          }else{
            this.finished[i] = "否"
          }
        }
      },
      httpErrorResponse => {
        this.message.error(JSON.stringify(httpErrorResponse.error))
      }
    )
  }

  public jumpToViewEditHomework(homeworkID: string) {
    window.open(`/universal/viewEditHomework?homeworkID=${homeworkID}`)
  }

}
