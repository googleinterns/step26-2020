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
import com.google.growpod.controllers.UserDao;
import com.google.growpod.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles user entities on the server. */
@WebServlet({"/user", "/user/*"})
public class UserServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private UserDao dao;

  private static final String CURRENT_USER_ARG = "current";
  private static final String GARDEN_LIST_ARG = "garden-list";
  private static final String GARDEN_ADMIN_LIST_ARG = "garden-admin-list";
  private static final String CURRENT_USER_KEY =
      "1"; // TODO(Issue #34): Replace value once oauth works

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
    assert (uriList.length >= 2 && uriList[1].equals("user"));

    if (uriList.length < 3) {
      response.sendError(
          HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
      return;
    }

    // Replace 'current' user with logged-in user.
    String userId = uriList[2];
    if (userId.equals(CURRENT_USER_ARG)) {
      userId = CURRENT_USER_KEY;
    }

    // Dispatch based on method specified.
    // /user/{id}
    if (uriList.length == 3) {
      User user = dao.getUserById(userId);
      if (user == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userId);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(user));
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals(GARDEN_LIST_ARG)) {
        // /user/{id}/garden-list
        List<String> list = dao.getUserGardenListById(userId);

        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userId);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals(GARDEN_ADMIN_LIST_ARG)) {
        // /user/{id}/garden-admin-list
        List<String> list = dao.getUserGardenAdminListById(userId);

        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + userId);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      }

      response.sendError(
          HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
    }
  }

  /**
   * Processes HTTP POST requests for the /user servlet. Dispatches functionality based on structure
   * of POST request.
   *
   * @param request Information about the POST Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList.length >= 2 && uriList[1].equals("user"));

    if (uriList.length == 2) {
      // /user
      response.setContentType("application/json");
      String json = request.getParameter("userData");
      User userData = new Gson().fromJson(json, User.class);
      dao.addToDatastore(userData);
      return;
    }

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
    if (uriList.length == 5) {
      if (uriList[3].equals(GARDEN_LIST_ARG)) {
        // /user/{uid}/garden-list/{gid}
        // TODO (Issue #34) Authenticate user.
        boolean result = dao.addGarden(userKey, uriList[4]);
        if (!result) {
          response.sendError(
              HttpServletResponse.SC_NOT_FOUND,
              "Not found - user: " + userKey + " garden: " + uriList[4]);
          return;
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + uriList[4] + "}");
        return;
      }
    }
    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /**
   * Processes HTTP DELETE requests for the /user servlet. Dispatches functionality based on
   * structure of DELETE request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
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
      // TODO Delete user provided owner is current user.
      return;
    }

    if (uriList.length == 5) {
      if (uriList[3].equals(GARDEN_LIST_ARG)) {
        // /user/{uid}/garden-list/{gid}
        // TODO (Issue #34) Verify user
        boolean status = dao.deleteFromUserGardenList(userKey, uriList[4]);
        if (!status) {
          // Nothing to delete
          response.sendError(
              HttpServletResponse.SC_NOT_FOUND,
              "Invalid garden: " + uriList[4] + " of user: " + userKey);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + uriList[4] + "}");
        return;
      }
      // If the uriList does not match the above two methods, fall through.
    }
    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /** Getters and Setters for data access object. */
  public UserDao getDao() {
    return dao;
  }

  public void setDao(UserDao dao) {
    this.dao = dao;
  }
}
