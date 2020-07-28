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

package com.google.growpod.servlets;

import com.google.cloud.datastore.DatastoreOptions;
import com.google.growpod.LoadTestData;
import com.google.growpod.controllers.UserDao;
import com.google.growpod.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  Map<String, User> userMap = new HashMap<String, User>();

  private UserDao dao;
  private LoadTestData data;

  private static final String CURRENT_USER_ARG = "current";
  private static final String GARDEN_LIST_ARG = "garden-list";
  private static final String GARDEN_ADMIN_LIST_ARG = "garden-admin-list";
  private static final String CURRENT_USER_KEY = "1";

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    DatastoreOptions datastoreInstance = DatastoreOptions.getDefaultInstance();
    this.dao = new UserDao(datastoreInstance);
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
    if (userKey.equals(CURRENT_USER_ARG)) {
      userKey = CURRENT_USER_KEY;
    }

    // Dispatch based on method specified.
    // /user/{id}

    if (uriList.length == 3) {
      User user = dao.getUserById(userKey);
      // User user = getUserById(uriList[2]);
      if (user == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(user));
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals(GARDEN_LIST_ARG)) {
        // /user/{id}/garden-list
        List<String> list = dao.getUserGardenListById(userKey);

        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals(GARDEN_ADMIN_LIST_ARG)) {
        // /user/{id}/garden-admin-list
        List<String> list = dao.getUserGardenAdminListById(userKey);

        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userKey);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      }

      response.getWriter().println("{ \"status\": \"ok\", \"value\": \"test\" }");

      response.sendError(
          HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
    }
  }
  /**
   * Retrieves a user in the database by id, or null if said id does not exist.
   *
   * @param id the user's id
   * @return the user with id's data or null.
   */
  private User getUserById(String id) {
    // MOCK IMPLEMENTATION
    if (id.equals("current")) {
      return userMap.get(CURRENT_USER_KEY);
    }
    return userMap.get(id);
  }

  /** Getters and Setters for data access object. */
  public UserDao getDao() {
    return dao;
  }

  public void setDao(UserDao dao) {
    this.dao = dao;
  }

  /**
   * Currently this Post function will get the user data in the form of a Json object,turn the Json
   * object into a User object and call a function that will in Datastore.
   *
   * @param req Information about the POST request
   * @param resp Information about the servlet's response
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Gson gson = new Gson();
    resp.setContentType("application/json");
    String jsonString = req.getParameter("userData");
    User userData = gson.fromJson(jsonString, User.class);
    System.out.println(userData + "userdata");
    dao.addToDS(userData);
  }
}
