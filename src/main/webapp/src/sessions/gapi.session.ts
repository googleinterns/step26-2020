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
import {CLIENT_ID} from '../app/SensitiveData';
import {CALENDAR_API_KEY} from '../app/SensitiveData';
import {TaskComponent} from '../app/calendar-task/task.component';
import {CLIENT_ID} from '../app/SensitiveData';

const GAPI_CLIENT_ID = CLIENT_ID;
const API_KEY = CALENDAR_API_KEY

const DISCOVERY_DOCS = [
  'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest',
];
const SCOPES = 'https://www.googleapis.com/auth/calendar';

declare const gapi: any;

@Injectable()
export class GapiSession {
  public auth2: any;
  hasConsent = false;
  tasks = new TaskComponent();

  constructor(private zone: NgZone) {}

  ngOnInit() {
    this.loadClient();
  }

  /**
   * Load the Calendar API client and provide authentication
   */
  loadClient(): void {
    gapi.load('client:auth2', () => {
      gapi.auth2
        .init({
          apiKey: API_KEY,
          clientId: GAPI_CLIENT_ID,
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
   * Returns whether user is signed in with Google
   */
  get isSignedIn(): boolean {
    return this.auth2.isSignedIn.get();
  }

  /**
   * Returns whether the user has accepted the consent prompt in the current session
   */
  get consent(): boolean {
    return this.hasConsent;
  }

  /**
   * Google Authentication; displays consent prompt for Google Calendar API
   */
  signIn() {
    return this.auth2
      .signIn({
        prompt: 'consent',
      })
      .then(() => {
        this.hasConsent = true;
      });
  }

  /**
   * Sign out from Google Auth
   */
  signOut(): void {
    this.auth2.signOut();
  }

  /**
   * Retrieve calendar events given a date and call function to display the events in the page
   * @param selectedDate - start date and time (min)
   * @param selectedDateMax - end of the day of selected date (max)
   */
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

  /**
   * Get the start time of the event otherwise return an All Day string
   * @param dateTime: string containing date and time
   */
  getEventTime(dateTime: string): string {
    if (dateTime) {
      const dateTimeSplit = dateTime.split('T');
      const time = dateTimeSplit[1].split('-', 1);
      return time[0];
    } else {
      return 'All Day';
    }
  }

  /** 
   * Inserts a new calendar event into the users' primary calendar
   * @param title - name of the event (known as summary in the calendar api)
   * @param startDateTime - start date and time of the event in ISO format
   * @param endDateTime - end date and time of the event in ISO format
   * @param timezone - set the timezone for the time
   * @param participants - members invited to event
   * @param description - (optional) description of the event
  */
  createEvent(title: string, startDateTime: string, endDateTime: string, timezone: string, participants: string[], description?: string) {
    if(typeof description!='undefined' && description) {
      description = ' ';
    }

    // Participants are added to the event in the following format
    let attendees = [];

    participants.forEach((participant) => {
       attendees.push({'email': participant});
    });

    const event = {
      'summary': title,
      'description': description,
      'start': {
        'dateTime': startDateTime,
        'timeZone': timezone
      },
      'end': {
        'dateTime': endDateTime,
        'timeZone': timezone
      },
      'attendees': attendees
    };

    const request = gapi.client.calendar.events.insert({
      'calendarId': 'primary',
      'resource': event
    });

    request.execute();
  }
}