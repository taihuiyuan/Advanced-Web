import {Component, OnInit} from '@angular/core';
import {CommonService} from "../../../../services/common/common.service";
import {HttpClient} from "@angular/common/http";
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-all-user',
  templateUrl: './all-user.component.html',
  styleUrls: ['./all-user.component.css']
})
export class AllUserComponent implements OnInit {

  public users: Array<any> = []

  public searchKey = ''


  constructor(private commonService: CommonService,
              private http: HttpClient,
              private message: NzMessageService) {
  }

  ngOnInit(): void {
    this.getAllUsers()
  }

  getAllUsers() {

    let interfaceUrl = "/allUsers";

    this.http.get(this.commonService.getBackendUrlPrefix() + interfaceUrl)
      .subscribe({
        next: response => {
          this.users = response as Array<any>
        },
        error: error => {
          this.message.error(JSON.stringify(error))
        }
      })

  }

}
