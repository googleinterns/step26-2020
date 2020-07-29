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

import {NgZone, Injectable} from '@angular/core';
import {UserRepository} from '../repositories/user.repository';
import {environment} from '../environments/environment';
import {TaskComponent} from '../app/calendar-task/task.component';

const CLIENT_ID =
  '397696466543-0biqdptbuhjmkjmakg2mo2dsov74dl0s.apps.googleusercontent.com';
const API_KEY = environment.calendar_key;

const DISCOVERY_DOCS = [
  'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest',
];
const SCOPES = 'https://www.googleapis.com/auth/calendar';

declare const gapi: any;

@Injectable()
export class GapiSession {
  public auth2: any;
  userRepository: UserRepository;
  hasConsent = false;
  tasks = new TaskComponent();

  constructor(private zone: NgZone) {
    this.userRepository = new UserRepository();
  }

  ngOnInit() {
    this.loadClient();
  }

  // add docs
  loadClient(): void {
    gapi.load('client:auth2', () => {
      gapi.auth2
        .init({
          apiKey: API_KEY,
          clientId: CLIENT_ID,
          discoveryDocs: DISCOVERY_DOCS,
          scope: SCOPES,
        })
        .then(auth => {
          this.zone.run(() => {
            this.auth2 = auth;
          });
        });
    });
  }

  /**
   * Include docs
   */
  get isSignedIn(): boolean {
    return this.auth2.isSignedIn.get();
  }

  get consent(): boolean {
    return this.hasConsent;
  }

  /**
   * Include docs
   */
  signIn() {
    return this.auth2
      .signIn({
        prompt: 'consent',
      })
      .then((googleUser: gapi.auth2.GoogleUser) => {
        this.userRepository.add(googleUser.getBasicProfile());
        this.hasConsent = true;
      });
  }

  signOut(): void {
    this.auth2.signOut();
  }

  listEvents(selectedDate: string, selectedDateMax: string) {
    gapi.client.load('calendar', 'v3', () => {
      gapi.client.calendar.events
        .list({
          calendarId: 'primary',
          timeMin: selectedDate,
          timeMax: selectedDateMax,
          singleEvents: true,
          orderBy: 'startTime',
        })
        .then(response => {
          const events = response.result.items;
          this.tasks.clearTasks();
          if (events.length > 0) {
            for (let i = 0; i < events.length; i++) {
              const event = events[i];
              const eventTime = this.getEventTime(event.start.dateTime);
              this.tasks.createTaskElement(
                eventTime,
                event.summary,
                event.attendees,
                event.description
              );
            }
          }
        });
    });
  }

  getEventTime(dateTime: string): string {
    if(dateTime) {
      const dateTimeSplit = dateTime.split('T');
      return dateTimeSplit[1].split('-', 1);
    }
    else {
      return "All Day";
    }
  }
}
