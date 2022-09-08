import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NzUploadChangeParam, NzUploadFile} from "ng-zorro-antd/upload";
import {NzMessageService} from "ng-zorro-antd/message";
import {CommonService} from "../../../../../services/common/common.service";
import {HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-upload-course-file',
  templateUrl: './upload-course-file.component.html',
  styleUrls: ['./upload-course-file.component.css']
})
export class UploadCourseFileComponent implements OnInit {

  @Input()
  public courseCode: string = ''

  public isModalVisible: boolean = false

  public myHeaders = {'Authorization': this.commonService.getJwt()}

  @Output()
  public uploadSuccess = new EventEmitter()

  /**
   * 根据上传的文件名，获得上传的url
   * @param nzUploadFile
   */
  public uploadAction = (nzUploadFile: NzUploadFile) => {
    console.log(nzUploadFile)
    return this.commonService.getBackendUrlPrefix() + `/${this.courseCode}/files/${nzUploadFile.name}`
  }

  constructor(private msg: NzMessageService,
              public commonService: CommonService) {
  }

  ngOnInit(): void {
  }

  showModal(): void {
    this.isModalVisible = true;
  }

  handleChange({file, fileList}: NzUploadChangeParam): void {
    const status = file.status;
    if (status !== 'uploading') {
    }
    if (status === 'done') {
      this.msg.success(`${file.name} 上传成功`);
      this.uploadSuccess.emit()
    } else if (status === 'error') {
      this.msg.error(`${file.name} 上传失败`);
    }
  }

  handleCancel(): void {
    this.isModalVisible = false;
  }

  fileUploadInterfaceUrl() {
    return this.commonService.getBackendUrlPrefix() + `/${this.courseCode}/files`
  }

}
