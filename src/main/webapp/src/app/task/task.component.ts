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
export class TaskComponent implements OnInit {
  displayInfo: TaskInfo | null;

  constructor(private httpClient: HttpClient) {
    this.createTaskElement();
  }

  ngOnInit(): void {}

  getTaskInfo(): Observable<HttpResponse<TaskInfo>> {
    return this.httpClient.get<TaskInfo>('/task', {
      observe: 'response',
      responseType: 'json',
    });
  }

  createTaskElement(): void {
    this.getTaskInfo().subscribe({
      next: response => {
        this.displayInfo = response.body;
      },
    });
  }
}