import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})

/**
 * 这个类用于组件之间的通讯
 */
export class CommunicationService {

  private emitMyCourseChangeSource = new Subject<any>();
  public myCourseChangeEmitted = this.emitMyCourseChangeSource.asObservable();

  constructor() {
  }

  public emitMyCourseChange() {
    this.emitMyCourseChangeSource.next(undefined)
  }
}
