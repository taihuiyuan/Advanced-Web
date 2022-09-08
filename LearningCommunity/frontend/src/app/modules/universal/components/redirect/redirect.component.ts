import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CommonService} from "../../../../services/common/common.service";

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.css']
})
export class RedirectComponent implements OnInit {

  constructor(private router: Router,
              private commonService: CommonService) {
  }

  ngOnInit(): void {
    if (!this.commonService.getJwt()) {
      this.router.navigate(['universal', 'userLogin'])
    }

    let role = this.commonService.getRoleByJwt();
    switch (role.toLowerCase()) {
      case 'teacher':
        this.router.navigate(['teacher'])
        break
      case 'ta':
        this.router.navigate(['ta'])
        break
      case 'student':
        this.router.navigate(['student'])
        break
      default:
        this.router.navigate(['universal', 'userLogin'])

    }

  }

}
