import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-text-truncate',
  templateUrl: './text-truncate.component.html',
  styleUrls: ['./text-truncate.component.css']
})
export class TextTruncateComponent implements OnInit {

  @Input()
  public text: string = ''

  @Input()
  public maxLen: number = 10

  constructor() {
  }

  ngOnInit(): void {
  }

  public getTruncated() {
    if (!this.text) {
      return ''
    }

    let textNoHtmlTag = this.text.replace(/<\/?.+?>/g, '').replace(/ /g, ''); // req为输入，res为输出

    if (textNoHtmlTag.length > 10) {
      return textNoHtmlTag.substring(0, 10) + "..."
    } else {
      return textNoHtmlTag;
    }
  }

}
