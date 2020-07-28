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

@Component({
  selector: 'calendar-tasks',
  templateUrl: './task.component.html',
  styleUrls: ['../common/growpod-page-styles.css'],
})
export class TaskComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  /**
   * Update task component by appending a new task element
   */
  addTaskToComponent(task: HTMLElement): void {
    const taskContainer = document.getElementById('all-tasks');
    taskContainer.appendChild(task);
  }

  /**
   * Create a task element based on information provided
   * @param time - event time on ISO format
   * @param title - event title
   * @param members - list of participants emails
   * @param description - (optional) event description
   */
  createTaskElement(
    time: string,
    title: string,
    members?: any[],
    description?: string
  ): HTMLElement {
    const matCard = document.createElement('mat-card');

    const matTitle = document.createElement('mat-card-title');
    matTitle.appendChild(document.createTextNode(time + ' | ' + title));

    const matMembers = document.createElement('h3');
    let allMembers = 'general event';
    if (members) {
      allMembers = this.membersToString(members);
    }
    matMembers.appendChild(document.createTextNode('Member(s): ' + allMembers));

    matCard.appendChild(matTitle);
    matCard.appendChild(matMembers);

    // If description was provided, add it to mat card
    if (description) {
      const matDescription = document.createElement('mat-card-content');
      matDescription.innerHTML = description;
      matCard.appendChild(matDescription);
    }

    return matCard;
  }

  /**
   * Given a list of members, return all nicknames into a single string
   * @param members - list of members as emails
   */
  membersToString(members: any[]): string {
    let result = '';
    for (let i = 0; i < members.length; i++) {
      const currMember = members[i].email.split('@', 1);
      if (i === members.length - 1) {
        result += currMember;
      } else {
        result += currMember + ',';
      }
    }
    return result;
  }
}
