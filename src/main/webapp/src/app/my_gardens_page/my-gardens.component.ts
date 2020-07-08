import {Component, OnInit} from '@angular/core';
import {OwlOptions} from 'ngx-owl-carousel-o';

@Component({
  selector: 'MyGardens',
  templateUrl: './my-gardens.component.html',
  styleUrls: [
    './my-gardens.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class MyGardensComponent implements OnInit {
  /** Option to display a certain number of slides based off of browser window size along with
   * some style options
   */
  customOptions: OwlOptions = {
    responsive: {
      0: {
        items: 1,
      },
      400: {
        items: 2,
      },
    },
    nav: true,
    margin: 20,
  };
  ngOnInit() {}
}
