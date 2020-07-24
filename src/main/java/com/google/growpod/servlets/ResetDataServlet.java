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

import com.google.cloud.datastore.DatastoreOptions;
import com.google.growpod.LoadTestData;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet to reset all user data to presets. */
@WebServlet("/reset-data")
public class ResetDataServlet extends HttpServlet {

  static final long serialVersionUID = 2L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    DatastoreOptions instance = DatastoreOptions.getDefaultInstance();

    // Deletes and repopulates everything.
    LoadTestData.clear(instance);
    LoadTestData.load(instance);

    response.setContentType("application/json;");
    response.getWriter().println("{}");
  }
}
