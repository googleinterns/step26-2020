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

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.growpod.controllers.UserController;
import com.google.growpod.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles user entities on the server.
 *
 * <p>API DOCUMENTATION: /user/{id} - {id} -- A user id, or `current` for the logged in user GET:
 * Retrieves the user data structure for {id} - No parameters - Returns data in JSON format along
 * with (200 OK), otherwise (404 NOT FOUND) /user/{id}/garden-list - {id} -- A user id, or `current`
 * for the logged in user GET: Retrieves the list of gardens user {id} is a member of - No
 * parameters - Returns data in JSON format along with (200 OK), otherwise (404 NOT FOUND)
 */
@WebServlet({"/user", "/user/*"})
public class UserServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private Datastore datastore;
  private UserController controller;

  private static final String CURRENT_USER_KEY = "0";

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    this.datastore = DatastoreOptions.getDefaultInstance().getService();
    this.controller = new UserController(datastore);
  }

  /**
   * Processes HTTP GET requests for the /user servlet. Dispatches functionality based on structure
   * of GET request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList[1].equals("user"));

    if (uriList.length < 3) {
      response.sendError(
          HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
      return;
    }

    // Replace 'current' user with logged-in user.
    String userKey = uriList[2];
    if (userKey.equals("current")) {
      userKey = CURRENT_USER_KEY;
    }

    // Dispatch based on method specified.
    // /user/{id}
    if (uriList.length == 3) {
      User user = controller.getUserById(userKey);
      if (user == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(user));
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals("garden-list")) {
        // /user/{id}/garden-list
        List<String> list = controller.getUserGardenListById(userKey);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals("garden-admin-list")) {
        // /user/{id}/garden-admin-list
        List<String> list = controller.getUserGardenAdminListById(userKey);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      }
    }
    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /**
   * Getters and Setters for connected objects
   */
  public Datastore getDatastore() {
    return datastore;
  }

  public void setDatastore(Datastore datastore) {
    this.datastore = datastore;
  }

  public UserController getController() {
    return controller;
  }

  public void setController(UserController controller) {
    this.controller = controller;
  }
}
