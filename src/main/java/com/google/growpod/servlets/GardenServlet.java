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
import com.google.growpod.controllers.GardenDao;
import com.google.growpod.data.Garden;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles garden entities on the server.
 *
 */
@WebServlet({"/garden", "/garden/*"})
public class GardenServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private GardenDao dao;

  private static final String USER_LIST_ARG = "user-list";
  private static final String PLANT_LIST_ARG = "plant-list";

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    DatastoreOptions datastoreInstance = DatastoreOptions.getDefaultInstance();
    this.dao = new GardenDao(datastoreInstance);
  }

  /**
   * Processes HTTP GET requests for the /garden servlet. Dispatches functionality based on
   * structure of GET request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList[1].equals("garden"));

    // Dispatch based on method specified.
    // /garden/{id}
    if (uriList.length == 3) {
      Garden garden = dao.getGardenById(uriList[2]);
      if (garden == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + uriList[2]);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(garden));
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals(USER_LIST_ARG)) {
        // /garden/{id}/user-list
        List<String> list = dao.getGardenUserListById(uriList[2]);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + uriList[2]);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals(PLANT_LIST_ARG)) {
        // /garden/{id}/plant-list
        List<String> list = dao.getGardenPlantListById(uriList[2]);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + uriList[2]);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      }
      // If the uriList does not match the above two methods, fall through.
    }
    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /**
   * Processes HTTP DELETE requests for the /garden servlet. Dispatches functionality based on
   * structure of DELETE request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList[1].equals("garden"));

    // Dispatch based on method specified.
    // /garden/{id}
    if (uriList.length == 3) {
      // TODO Delete garden provided owner is current user.
      return;
    }

    if (uriList.length == 5) {
      if (uriList[3].equals(USER_LIST_ARG)) {
        // /garden/{gid}/user-list/{uid}
        // TODO (Issue #34) Verify user
        boolean status = dao.deleteFromGardenUserList(uriList[2], uriList[4]);
        if (!status) {
          // Nothing to delete
          response.setStatus(HttpServletResponse.SC_NO_CONTENT);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + uriList[4] + "}");
        return;
      } else if (uriList[3].equals(PLANT_LIST_ARG)) {
        // /garden/{gid}/plant-list/{pid}
        boolean status = dao.deleteFromGardenPlantList(uriList[2], uriList[4]);
        if (!status) {
          response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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
  public GardenDao getDao() {
    return dao;
  }

  public void setDao(GardenDao dao) {
    this.dao = dao;
  }
}
