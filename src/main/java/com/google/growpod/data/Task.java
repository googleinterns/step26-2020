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

package com.google.growpod.data;

import java.util.List;

/** Task data class. */
public class Task {

  /* The title of the event */
  private String title;

  /* Start time for the event */
  private String time;

  /* List of people needed on the event */
  private List<String> participants;

  /* The task's detailed description. Optional */
  private String description;

  public Task(String title, String time, List<String> participants, String description) {
    this.title = title;
    this.time = time;
    this.participants = participants;
    this.description = description;
  }

  /* Getters */
  public String getTitle() {
    return this.title;
  }

  public String getTime() {
    return this.time;
  }

  public List<String> getParticipants() {
    return this.participants;
  }

  public String getDescription() {
    return this.description;
  }

  /* Setters */
  public void setTitle(String title) {
    this.title = title;
  }

  public void setTime(String time) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.title = title;
  }
}
