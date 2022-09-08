import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {CommonService} from "../services/common/common.service";
import {Injectable} from "@angular/core";

@Injectable()
export class AddTokenHttpInterceptor implements HttpInterceptor {

  constructor(private commonService: CommonService) {
  }


  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(this.addToken(req, this.commonService.getJwt()))
  }

  private addToken(req: HttpRequest<any>, token: string): HttpRequest<any> {
    return req.clone({setHeaders: {Authorization: token}});
  }

}
