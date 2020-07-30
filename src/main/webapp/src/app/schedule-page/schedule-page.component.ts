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

import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GapiSession } from '../../sessions/gapi.session';
import { DatepickerComponent } from '../datepicker/datepicker.component';
import { Garden } from '../model/garden.model';

@Component({
  selector: 'schedule-page',
  templateUrl: './schedule-page.component.html',
  styleUrls: [
    './schedule-page.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class SchedulePageComponent implements OnInit {
  @ViewChild('datepickerElem', { static: false })
  datepickerElem: DatepickerComponent;
  displayInfo: Garden | null;
  errorMessage: string;

  constructor(private gapiSession: GapiSession, private route: ActivatedRoute, private httpClient: HttpClient) { 
    const idArg = route.snapshot.paramMap.get('id');
    const id = idArg ?? 'current';
    this.createGardenProfile(id);
  }

  ngOnInit(): void { }

  /**
   * Populates component to create a garden, or shows an
   * error message if profile does not exist.
   *
   * @param garden The garden requested.
   */
  createGardenProfile(garden: string): void {
    this.getGardenInfo(garden).subscribe({
      next: response => {
        // Successful responses are handled here.
        this.displayInfo = response.body;
      },
      error: error => {
        // Error messages are handled here.
        this.displayInfo = null;
        this.errorMessage = 'Cannot see garden for garden id: ' + garden;
        console.log(new Error(error));
      },
    });
  }

   /**
   * Gets garden information for the specified garden from
   * the server. Returns an observable HTTP response.
   *
   * Performs GET: /garden/{garden}
   *
   * @param garden - The garden requested from the server.
   * @return - the http response.
   */
  getGardenInfo(garden: string): Observable<HttpResponse<Garden>> {
    return this.httpClient.get<Garden>('/garden/' + garden, {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Display consent popup for calendar API, list event based on selected date
   * Consent prompt only appears once per session
   */
  fetchCalendarEvents() {
    // Normal flow; check if user has consented and signed in
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
