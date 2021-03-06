// Copyright 2020 Google LLC
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

import {Component, OnInit, ViewChild} from '@angular/core';
import {GapiSession} from '../../sessions/gapi.session';
import {DatepickerComponent} from '../datepicker/datepicker.component';

@Component({
  selector: 'schedule-page',
  templateUrl: './schedule-page.component.html',
  styleUrls: [
    './schedule-page.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class SchedulePageComponent implements OnInit {
  @ViewChild('datepickerElem', {static: false})
  datepickerElem: DatepickerComponent;

  constructor(private gapiSession: GapiSession) {}

  ngOnInit(): void {}

  /**
   * Display consent popup for calendar API, list event based on selected date
   * Consent prompt only appears once per session
   */
  fetchCalendarEvents() {
    if (this.gapiSession.consent) {
      this.gapiSession.listEvents(
        this.datepickerElem.selectedDate,
        this.datepickerElem.selectedDateMax
      );
    } else {
      this.gapiSession.signIn().then(() => {
        this.gapiSession.listEvents(
          this.datepickerElem.selectedDate,
          this.datepickerElem.selectedDateMax
        );
      });
    }
  }
}
