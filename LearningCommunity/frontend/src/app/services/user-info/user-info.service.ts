import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpResponse} from "@angular/common/http";
import {CommonService} from "../common/common.service";
import {UserInfoDTO} from "./dtos/UserInfoDTO";
import {SuccessfulPostResponse} from "../../reqrsp/SuccessfulPostResponse";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {

  private myHeaders = new HttpHeaders().set("Authorization", this.commonService.getJwt());

  constructor(public http: HttpClient,
              public commonService: CommonService) {
  }

  /**
   * 查询自己的个人信息
   * @param next
   * @param error
   */
  public getSelfUserInfo(next: (httpResponse: HttpResponse<UserInfoDTO>) => void,
                         error: (httpErrorResponse: HttpErrorResponse) => void) {
    let interfaceUrl = '/userInfo'


    //一个trick，每次请求带一个date，拒绝浏览器缓存
    let params = new HttpParams().set("date", new Date().toString());

    this.http.get(
      this.commonService.getBackendUrlPrefix() + interfaceUrl,
      {headers: this.myHeaders, observe: "response", params: params}
    ).subscribe({
      next: next as (httpResponse: HttpResponse<any>) => void,
      error: error
    })
  }

  /**
   * 添加自己的个人信息
   * @param email
   * @param next
   * @param error
   */
  public updateSelfUserInfo(email: string,
                            next: (httpResponse: HttpResponse<SuccessfulPostResponse>) => void,
                            error: (httpErrorResponse: HttpErrorResponse) => void) {

    let interfaceUrl = '/updateUserInfo'
    const params = new HttpParams().set("email", email);
    this.http.put(
      this.commonService.getBackendUrlPrefix() + interfaceUrl,
      "",
      {headers: this.myHeaders, observe: 'response', params: params}
    ).subscribe({
      next: next as (httpResponse: HttpResponse<any>) => void,
      error: error
    })
  }


  /**
   * 获得自己的头像
   * @param next
   * @param error
   */
  public getSelfAvatarSrc(next: (httpResponse: HttpResponse<any>) => void,
                          error: (httpErrorResponse: HttpErrorResponse) => void) {

    let interfaceUrl = '/userAvatar'

    //一个trick，每次请求带一个随机数，拒绝浏览器缓存
    let params = new HttpParams().set("date", new Date().toString());

    this.http.get(
      this.commonService.getBackendUrlPrefix() + interfaceUrl,
      {headers: this.myHeaders, observe: 'response', params: params}
    ).subscribe({
      next: next,
      error: error
    })

  }

  /**
   * 更新自己的个人头像
   * @param avatarSrc
   * @param next
   * @param error
   */
  public updateSelfAvatarSrc(avatarSrc: string,
                             next: (httpResponse: HttpResponse<SuccessfulPostResponse>) => void,
                             error: (httpErrorResponse: HttpErrorResponse) => void) {

    let interfaceUrl = '/updateAvatar'
    this.http.put(
      this.commonService.getBackendUrlPrefix() + interfaceUrl,
      {avatar: avatarSrc},
      {headers: this.myHeaders, observe: 'response'}
    ).subscribe({
      next: next as (httpResponse: HttpResponse<any>) => void,
      error: error
    })
  }


  /**
   * 得到其他用户的头像的Observable
   * @param otherUsername 其他用户的用户名
   */
  public getOtherAvatarSrcObservable(otherUsername: string): Observable<HttpResponse<any>> {
    let interfaceUrl = '/otherAvatar'

    let params = new HttpParams().set("username", otherUsername)

    return this.http.get(
      this.commonService.getBackendUrlPrefix() + interfaceUrl,
      {headers: this.myHeaders, observe: 'response', params: params}
    )
  }

}
