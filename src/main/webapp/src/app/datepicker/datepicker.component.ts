// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import {Component, OnInit} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material/datepicker';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'datepicker',
  templateUrl: './datepicker.component.html',
})
export class DatepickerComponent implements OnInit {
  date = new FormControl(new Date());
  currDate: string = this.formatDate(this.date.value.toString());

  constructor() {}

  ngOnInit(): void {}

  /* Updates the displayed date whenever user chooses a date in the datepicker */
  updateCurrDate(event: MatDatepickerInputEvent<Date>): void {
    if (event.value) {
      this.currDate = this.formatDate(`${event.value}`);
    } else {
      this.currDate = '';
    }
  }

  /**
   * Formats a string containing a date to: {Day Abbreviation}: {Month} {Day}, {Year}
   * @param date - a string separated by spaces with the following info {Day} {Month} {Day} {Year} {Hour:Minute:Seconds} {GMT} {Timezone}
   */
  formatDate(date: string): string {
    let formattedDate = '';
    if (date) {
      const splits = date.split(' ');
      formattedDate =
        splits[0] + ': ' + splits[1] + ' ' + splits[2] + ', ' + splits[3];
    }
    return formattedDate;
  }
}
