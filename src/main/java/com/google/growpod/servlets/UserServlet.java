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

import com.google.growpod.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  /** Static test data. */
  private static final String CURRENT_USER_KEY = "0";

  private static final Map<String, User> USER_MAP = createUserMap();

  private static Map<String, User> createUserMap() {
    Map<String, User> map = new HashMap<String, User>();
    map.put(
        "0",
        new User("0", "ladd@example.com", "David Ladd", "My SSN is: 143-46-6098", "New York, NY"));
    map.put(
        "1",
        new User("1", "caroqliu@google.com", "Caroline Liu", "Plants are fun", "New York, NY"));
    map.put(
        "2",
        new User("2", "friedj@google.com", "Jake Fried", "Plants are fun too", "New York, NY"));
    return Collections.unmodifiableMap(map);
  }

  private static final Map<String, List<String>> USER_GARDEN_LIST_MAP = createUserGardenListMap();

  private static Map<String, List<String>> createUserGardenListMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("0", Arrays.asList("0", "1"));
    map.put("1", Arrays.asList("1"));
    map.put("2", Arrays.asList("1"));
    return Collections.unmodifiableMap(map);
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

    // Dispatch based on method specified.
    // /user/{id}
    if (uriList.length == 3) {
      User user = getUserById(uriList[2]);
      if (user == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + uriList[2]);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(user));
      return;
    }

    // /user/{id}/garden-list
    if (uriList.length == 4) {
      List<String> list = getUserGardenListById(uriList[2]);
      if (list == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid user id: " + uriList[2]);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(list));
      return;
    }
    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /**
   * Retrieves a user in the database by id, or null if said id does not exist.
   *
   * @param id the user's id
   * @return the user with id's data or null.
   */
  private User getUserById(String id) {
    // MOCKUP IMPLEMENTATION
    if (id.equals("current")) {
      return USER_MAP.get(CURRENT_USER_KEY);
    }
    return USER_MAP.get(id);
  }

  /**
   * Retrieves a list of gardens the user with a given id is a member of. Returns an empty list if
   * the user is a member of no gardens, and null if the user does not exist.
   *
   * @param id the user's id
   * @return a list of gardens the user is a part of, or an empty list, or null.
   */
  private List<String> getUserGardenListById(String id) {
    // MOCKUP IMPLEMENTATION
    if (id.equals("current")) {
      return USER_GARDEN_LIST_MAP.get(CURRENT_USER_KEY);
    }
    return USER_GARDEN_LIST_MAP.get(id);
  }
}
