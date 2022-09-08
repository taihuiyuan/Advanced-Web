import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ViewCourseFileComponent} from "./view-course-file/view-course-file.component";

@Component({
  selector: 'app-course-file',
  templateUrl: './course-file.component.html',
  styleUrls: ['./course-file.component.css']
})
export class CourseFileComponent implements OnInit {

  @Input()
  public allowFileUpload: boolean = false

  @Input()
  public courseCode: string = ''

  @ViewChild(ViewCourseFileComponent)
  private viewCourseFile: ViewCourseFileComponent | undefined

  constructor() {
  }

  ngOnInit(): void {
  }

  onUploadSuccess() {
    this.viewCourseFile?.ngOnInit()
  }

}
