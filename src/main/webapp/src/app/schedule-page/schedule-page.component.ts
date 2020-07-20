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
import {FormControl} from '@angular/forms';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CalendarInfo} from '../model/calendarInfo.model';

@Component({
  selector: 'schedule-page',
  templateUrl: './schedule-page.component.html',
  styleUrls: [
    './schedule-page.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class SchedulePageComponent implements OnInit {
  date = new FormControl(new Date());
  serializedDate = new FormControl(new Date().toISOString());

  constructor(private httpClient: HttpClient) {
    this.showConsoleComments();
  }

  ngOnInit(): void {}

  /**
   * Gets user information for the specified user from
   * the server. Returns an observable HTTP response.
   *
   * Performs GET: /user/{user}
   *
   * @param user The user reqested from the server.
   * @return the http response.
   */
  getCalendarInfo(): Observable<HttpResponse<CalendarInfo>> {
    return this.httpClient.get<CalendarInfo>('/calendartest', {
      observe: 'response',
      responseType: 'json',
    });
  }
  // response types are different

  showConsoleComments(): void {
    console.log(this.getCalendarInfo());
    this.getCalendarInfo().subscribe({
      next: response => {
        console.log('success!');
      },
      error: error => {
        // Error messages are handled here.
        console.log('error');
      },
    });
  }
}
