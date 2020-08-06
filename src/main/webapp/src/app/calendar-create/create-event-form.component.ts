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

import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import {EventInfo} from '../calendar-event/event-info';
import {TIMEZONES} from './timezones';

@Component({
  selector: 'create-event-form',
  templateUrl: './create-event-form.component.html',
  styleUrls: ['../common/growpod-page-styles.css'],
})

/**
 * Form to create a calendar event
 */
export class CreateEventComponent implements OnInit {
  timezoneUS = TIMEZONES;

  // Information available on the form
  eventInfo: EventInfo = {
    title: undefined,
    dateTime: new Date(),
    startTime: undefined,
    endTime: undefined,
    timezone: undefined,
    participants: undefined,
    description: '',
  };

  members: string[];

  // Values will be update after sucessfully calling this.submit
  startDateTime: string;
  endDateTime: string;

  submitSuccess = false;
  eventGroup: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.eventGroup = new FormGroup({
      title: new FormControl(this.eventInfo.title, [
        Validators.required,
        Validators.minLength(1),
      ]),
      dateTime: new FormControl(this.eventInfo.dateTime, [Validators.required]),
      startTime: new FormControl(this.eventInfo.startTime, [
        Validators.required,
      ]),
      endTime: new FormControl(this.eventInfo.endTime, [Validators.required]),
      timezone: new FormControl(this.eventInfo.timezone, [Validators.required]),
      participants: new FormControl(this.eventInfo.participants, [
        Validators.pattern('.+@google.com'),
      ]),
      description: new FormControl(this.eventInfo.description),
    });
  }

  /**
   * Updates the values to the ones selected on the form
   * @param gardenName - current garden name; this wil help filter events when displaying
   */
  submit(gardenName: string): void {
    if (this.eventGroup.valid) {
      this.submitSuccess = true;
      this.eventInfo = this.eventGroup.value;
      this.eventInfo.title = '[' + gardenName + '] ' + this.eventInfo.title;

      if (this.eventInfo.participants) {
        this.members = this.eventInfo.participants.split(',');
      }

      this.startDateTime = this.getDateTime(
        this.eventInfo.dateTime.toISOString(),
        this.eventInfo.startTime
      );
      this.endDateTime = this.getDateTime(
        this.eventInfo.dateTime.toISOString(),
        this.eventInfo.endTime
      );
    }
  }

  /**
   * Combines the selected date with the assigned time
   * @param dateTime - contains date and the default time 00:00:00
   * @param time - contains time in a 24hr format
   */
  getDateTime(dateTime: string, time: string): string {
    const dateISO = dateTime.split('T', 1);
    // TODO: Create test for timezones and time
    const result = dateISO + 'T' + time + ':00-06:00';
    return result;
  }
}
