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
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TaskInfo} from '../model/task-info.model';

@Component({
  selector: 'task',
  templateUrl: './task.component.html',
  styleUrls: ['../common/growpod-page-styles.css'],
})

/**
 * This is a component that will display the tasks of a given date and garden
 *
 * Mockup: Current implementation does not take into account a date or garden and simply
 * displays a single random hardcoded task
 *
 * TODO: This page will take the arguments:
 * 'garden-id' - the unique id of a garden
 * 'date' - the date selected (default: current date)
 */
export class TaskComponent implements OnInit {
  displayInfo: TaskInfo | null;

  /**
   * Initializes the task component. Mockup implementation
   * does not consider arguments at this moment
   */
  constructor(private httpClient: HttpClient) {
    this.createTaskElement();
  }

  ngOnInit(): void {}

  /**
   * Gets the task information from the server. Returns an
   * observable HTTP response.
   */
  getTaskInfo(): Observable<HttpResponse<TaskInfo>> {
    return this.httpClient.get<TaskInfo>('/schedule', {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Populates component to show a task or shows a message indicating
   * that no tasks were found
   */
  createTaskElement(): void {
    this.getTaskInfo().subscribe({
      next: response => {
        this.displayInfo = response.body;
      },
    });
  }
}
