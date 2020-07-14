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

package com.google.growpod.servlets;

import com.google.growpod.data.Task;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles the tasks from a particular garden and date
 *
 * TODO: This is the mockup implementation; a future PR will implement calendar api and connect gardens together
 */

@WebServlet({"/schedule", "schedule/*"})
public class TaskServlet extends HttpServlet {

  private List<Task> tasks;

  /* Mockup implementation: Hardcoded tasks for testing */
  @Override
  public void init() {
    tasks = new ArrayList<>();
    List<String> hosts = new ArrayList();
    List<String> interns = new ArrayList();

    hosts.add("Caroline");
    hosts.add("Jake");

    interns.add("Stephanie");
    interns.add("Kayla");
    interns.add("Cody");

    tasks.add(
        new Task("Water plants", "8:30 am", hosts, "Don't forget to water the trees as well"));
    tasks.add(
        new Task(
            "Pick up apples",
            "3:00 pm",
            interns,
            "Feel free to grab a few for yourself, but please put them in their assigned boxes"));
    tasks.add(new Task("Send apples to the shelter", "4:30 pm", hosts, "Shelter closes at 5:30pm"));
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Display a random task in the schedule page
    Task task = tasks.get((int) (Math.random() * tasks.size()));

    response.setContentType("text/html;");
    response.getWriter().println(new Gson().toJson(task));
  }
}
