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
  styleUrls: ['./task.component.css', '../common/growpod-page-styles.css'],
})
export class TaskComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  /**
   * Clear tasks before adding any new elements
   */
  clearTasks(): void {
    const tasksContainer = document.getElementById('all-tasks');
    while (tasksContainer.firstChild) {
      tasksContainer.removeChild(tasksContainer.lastChild);
    }
  }

  /**
   * Create a task element based on information provided and add it to DOM
   * @param time - event time on ISO format
   * @param title - event title
   * @param members - (optional) list of participants emails
   * @param description - (optional) event description
   */
  createTaskElement(
    time: string,
    title: string,
    members?: any[],
    description?: string
  ): void {
    // Div element to represent the task card
    const card = document.createElement('div');
    card.className = 'task-card';

    // Add title which includes a start time (or an all day) and the title of the event
    const cardTitle = document.createElement('h2');
    cardTitle.appendChild(document.createTextNode(time + ' | ' + title));
    card.appendChild(cardTitle);

    // Include participants (optional) or set event as a general event for anyone
    const cardMembers = document.createElement('h3');
    let allMembers = 'general event (anyone)';
    if (members) {
      allMembers = this.membersToString(members);
    }
    cardMembers.appendChild(
      document.createTextNode('Member(s): ' + allMembers)
    );
    card.appendChild(cardMembers);

    // (Optional) If description was provided, add it to mat card
    if (description) {
      const cardDescription = document.createElement('p');
      cardDescription.innerHTML = description;
      card.appendChild(cardDescription);
    }

    // Add task card to the all tasks container
    const taskContainer = document.getElementById('all-tasks');
    taskContainer.appendChild(card);
  }

  /**
   * Given a list of members, return all nicknames into a single string
   * @param members - list of members as emails
   */
  membersToString(members: any[]): string {
    let usernames = members.map(m => m.email.split('@')[0]);
    return usernames.join(',');
  }
}
