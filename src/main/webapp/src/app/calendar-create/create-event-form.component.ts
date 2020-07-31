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

import { Component, OnInit } from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import { EventInfo }    from '../calendar-event/event-info';
import { TIMEZONES } from './timezones.ts'

@Component({
  selector: 'create-event-form',
  templateUrl: './create-event-form.component.html',
  styleUrls: ['../common/growpod-page-styles.css']
})

/**
 * Form to create a calendar event
 */
export class CreateEventComponent implements OnInit {
  timezoneUS = TIMEZONES;
  eventInfo : EventInfo = {
    title: undefined,
    dateTime: new Date(),
    startTime: undefined,
    endTime: undefined,
    timezone: undefined,
    participants: ['user@example.com'],
    description: '',
  };
  gardenGroup: FormGroup;
  

  constructor() {}

  ngOnInit():void {
    this.gardenGroup = new FormGroup({
      title: new FormControl(this.eventInfo.title, [
        Validators.required,
        Validators.minLength(1),
      ]),
     timezone: new FormControl(this.eventInfo.timezone, [
        Validators.required,
      ]),
    });
  }

  onSubmit():void { 
    if (this.gardenGroup.valid) {
      this.eventInfo = new EventInfo();
    }
  }

  

}
