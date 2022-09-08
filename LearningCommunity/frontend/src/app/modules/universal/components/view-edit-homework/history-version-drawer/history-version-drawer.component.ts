import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {BriefVersion} from "../../../../../services/homework/dtos/BriefVersion";
import {HomeworkService} from "../../../../../services/homework/homework.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {DetailedHomework} from "../../../../../services/homework/dtos/DetailedHomework";
import {firstValueFrom} from "rxjs";
import {UserInfoService} from "../../../../../services/user-info/user-info.service";

@Component({
  selector: 'app-history-version-drawer',
  templateUrl: './history-version-drawer.component.html',
  styleUrls: ['./history-version-drawer.component.css']
})
export class HistoryVersionDrawerComponent implements OnInit {

  public visible: boolean = false

  @Output()
  public homeworkContentChanged = new EventEmitter()

  @Input()
  public historyBriefVersions: BriefVersion[] | undefined;

  //提交历史版本的用户头像
  public avatars: string[] = []

  @Input()
  public detailedHomework: DetailedHomework | undefined;

  constructor(private homeworkService: HomeworkService,
              private message: NzMessageService,
              private userInfoService: UserInfoService) {
  }

  ngOnInit(): void {
    this.loadAvatars()
      .then()
      .catch(err => {
        console.log(err)
        this.message.error(JSON.stringify(err.error))
      })
  }

  close(): void {
    this.visible = false
  }

  open(): void {
    console.log(this.historyBriefVersions)
    this.visible = true
  }

  async loadAvatars() {
    let versions = this.historyBriefVersions as BriefVersion[]
    let avatars: string[] = []

    for (let i = 0; i < versions.length; i++) {
      let avatarLocal = (await firstValueFrom(this.userInfoService.getOtherAvatarSrcObservable(versions[i].fromUsername))).body['avatar']
      avatars.push(avatarLocal)
    }

    this.avatars = avatars
  }


  backtrackHistoryVersion(homeworkVersionID: string) {
    this.homeworkService.getDetailedVersion(
      homeworkVersionID,
      response => {
        this.message.success("回溯成功，请查看")
        this.homeworkContentChanged.emit(response.body?.content)
      },
      errorResponse => {
        this.message.error(JSON.stringify(errorResponse.error))
      }
    )
  }

  historyVersionDescription(version: BriefVersion) {
    return `${version.fromUsername} \n
            创建于：${version.createdTime}\n
            即将过期于：${version.expireTime}`
  }

}
