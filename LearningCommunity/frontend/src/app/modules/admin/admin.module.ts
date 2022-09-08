import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';


import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzBreadCrumbModule} from 'ng-zorro-antd/breadcrumb';
import {AdminHomeComponent} from './admin-home.component';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {NzDropDownModule} from 'ng-zorro-antd/dropdown';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {AddTeacherTaStudentComponent} from './components/add-student/add-teacher-ta-student.component';
import {RouterModule} from "@angular/router";
import {NzGridModule} from "ng-zorro-antd/grid";
import {NzInputModule} from "ng-zorro-antd/input";
import {NzFormModule} from "ng-zorro-antd/form";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NzSelectModule} from "ng-zorro-antd/select";
import {NzCheckboxModule} from "ng-zorro-antd/checkbox";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzRadioModule} from "ng-zorro-antd/radio";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AddTokenHttpInterceptor} from "../../interceptor/AddTokenHttpInterceptor";
import { AllUserComponent } from './components/all-user/all-user.component';
import {NzTableModule} from "ng-zorro-antd/table";


@NgModule({
  declarations: [
    AdminHomeComponent,
    AddTeacherTaStudentComponent,
    AllUserComponent,

  ],
    imports: [
        CommonModule,
        NzLayoutModule,
        NzBreadCrumbModule,
        NzMenuModule,
        NzDropDownModule,
        NzIconModule,
        RouterModule,
        NzGridModule,
        NzInputModule,
        NzFormModule,
        FormsModule,
        NzSelectModule,
        NzCheckboxModule,
        NzButtonModule,
        ReactiveFormsModule,
        NzRadioModule,
        NzTableModule
    ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AddTokenHttpInterceptor, multi: true}
  ]
})
export class AdminModule {
}
