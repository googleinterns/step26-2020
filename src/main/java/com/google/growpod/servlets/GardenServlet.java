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
import com.google.growpod.controllers.UserDao;
import com.google.growpod.data.Garden;
import com.google.growpod.data.Plant;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles garden entities on the server. */
@WebServlet({"/garden", "/garden/*"})
public class GardenServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private GardenDao dao;
  private UserDao userDao;

  private static final String USER_LIST_ARG = "user-list";
  private static final String PLANT_LIST_ARG = "plant-list";

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    DatastoreOptions datastoreInstance = DatastoreOptions.getDefaultInstance();
    this.dao = new GardenDao(datastoreInstance);
    this.userDao = new UserDao(datastoreInstance);
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
    assert (uriList.length >= 2 && uriList[1].equals("garden"));

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
      String gardenId = uriList[2];
      if (uriList[3].equals(USER_LIST_ARG)) {
        // /garden/{id}/user-list
        List<String> list = dao.getGardenUserListById(gardenId);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + gardenId);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals(PLANT_LIST_ARG)) {
        // /garden/{id}/plant-list
        List<String> list = dao.getGardenPlantListById(gardenId);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + gardenId);
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
   * Processes HTTP POST requests for the /garden servlet. Dispatches functionality based on
   * structure of POST request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList.length >= 2 && uriList[1].equals("garden"));

    // Dispatch based on method specified.
    // /garden
    if (uriList.length == 2) {
      response.setContentType("application/json");
      String json = request.getParameter("gardenData");
      Garden gardenData = new Gson().fromJson(json, Garden.class);
      gardenData.setAdminId(userDao.getKeyByEmail(gardenData.getAdminId()));
      dao.addGardenToDatastore(gardenData);
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals(PLANT_LIST_ARG)) {
        // /garden/{gid}/plant-list
        // TODO (Issue #34) Verify user
        String gardenId = uriList[2];
        String json = getBody(request);
        Plant plant = new Gson().fromJson(json, Plant.class);
        Garden garden = dao.getGardenById(gardenId);
        if (garden == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + gardenId);
          return;
        }
        String key = dao.addPlant(gardenId, plant);
        if (key == null) {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid plant body");
          return;
        }

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + key + "}");
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
  public void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList.length >= 2 && uriList[1].equals("garden"));

    // Dispatch based on method specified.
    // /garden/{id}
    if (uriList.length == 3) {
      // TODO Delete garden provided owner is current user.
      return;
    }

    if (uriList.length == 5) {
      String gardenId = uriList[2];
      if (uriList[3].equals(USER_LIST_ARG)) {
        // /garden/{gid}/user-list/{uid}
        // TODO (Issue #34) Verify user
        String userId = uriList[4];
        boolean status = dao.deleteUser(gardenId, userId);
        if (!status) {
          // Nothing to delete
          response.sendError(
              HttpServletResponse.SC_NOT_FOUND,
              "Invalid user: " + userId + " of garden: " + gardenId);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + userId + "}");
        return;
      } else if (uriList[3].equals(PLANT_LIST_ARG)) {
        // /garden/{gid}/plant-list/{pid}
        String plantId = uriList[4];
        boolean status = dao.deletePlant(gardenId, plantId);
        if (!status) {
          response.sendError(
              HttpServletResponse.SC_NOT_FOUND,
              "Invalid plant: " + gardenId + " of garden: " + plantId);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println("{\"id\":" + plantId + "}");
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

  /**
   * Reads POST request body into string.
   *
   * @param request Http Servlet Request.
   * @return The request body.
   */
  private String getBody(HttpServletRequest request) throws IOException {
    StringBuilder body = new StringBuilder();
    BufferedReader reader = request.getReader();
    String line = reader.readLine();
    while (line != null) {
      body.append(line);
      // Here because readLine removes trailing newline which may separate JSON tokens.
      body.append(System.lineSeparator());
      line = reader.readLine();
    }
    return body.toString();
  }
}
