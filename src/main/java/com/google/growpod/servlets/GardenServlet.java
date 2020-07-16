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
import com.google.growpod.controllers.GardenController;
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
 * <p>API DOCUMENTATION: /garden/{id} {id} -- A garden UUID GET: Retrieves the garden data structure
 * for {id} No parameters Returns data in JSON format along with (200 OK), otherwise (404 NOT FOUND)
 * /garden/{id}/plant-list {id} -- A garden UUID GET: Retrieves all plants currently on {id} garden
 * No parameters Returns data in JSON format along with (200 OK), otherwise (404 NOT FOUND)
 * /garden/{id}/user-list {id} -- A garden UUID GET: Retrieves all users currently on {id} garden No
 * parameters Returns data in JSON format along with (200 OK), otherwise (404 NOT FOUND)
 */
@WebServlet({"/garden", "/garden/*"})
public class GardenServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private Datastore datastore;
  private GardenController controller;

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    this.datastore = DatastoreOptions.getDefaultInstance().getService();
    this.controller = new GardenController(datastore);
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
      Garden garden = controller.getGardenById(uriList[2]);
      if (garden == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + uriList[2]);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(garden));
      return;
    }

    if (uriList.length == 4) {
      if (uriList[3].equals("user-list")) {
        // /garden/{id}/user-list
        List<String> list = controller.getGardenUserListById(uriList[2]);
        if (list == null) {
          response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid garden id: " + uriList[2]);
          return;
        }
        response.setContentType("application/json;");
        response.getWriter().println(new Gson().toJson(list));
        return;
      } else if (uriList[3].equals("plant-list")) {
        // /garden/{id}/plant-list
        List<String> list = controller.getGardenPlantListById(uriList[2]);
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

  /** Getters and Setters for connected objects. */
  public Datastore getDatastore() {
    return datastore;
  }

  public void setDatastore(Datastore datastore) {
    this.datastore = datastore;
  }

  public GardenController getController() {
    return controller;
  }

  public void setController(GardenController controller) {
    this.controller = controller;
  }
}
