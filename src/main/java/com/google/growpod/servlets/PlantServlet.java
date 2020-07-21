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

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.growpod.controllers.PlantDao;
import com.google.growpod.data.Plant;
import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that handles plant entities on the server.
 *
 * <p>API DOCUMENTATION: /plant/{id} {id} -- A plant UUID GET: Retrieves the plant data structure
 * for {id} No parameters Returns data in JSON format along with (200 OK), otherwise (404 NOT FOUND)
 */
@WebServlet({"/plant", "/plant/*"})
public class PlantServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private Datastore datastore;
  private PlantDao dao;

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    this.datastore = DatastoreOptions.getDefaultInstance().getService();
    this.dao = new PlantDao(datastore);
  }

  /**
   * Processes HTTP GET requests for the /plant servlet. Dispatches functionality based on structure
   * of GET request.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* uriList will have "" as element 0 */
    String[] uriList = request.getRequestURI().split("/");
    assert (uriList[1].equals("plant"));

    // Dispatch based on method specified.
    // /plant/{id}
    if (uriList.length == 3) {
      Plant plant = dao.getPlantById(uriList[2]);
      if (plant == null) {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid plant id: " + uriList[2]);
        return;
      }
      response.setContentType("application/json;");
      response.getWriter().println(new Gson().toJson(plant));
      return;
    }

    response.sendError(
        HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unimplemented: " + request.getRequestURI());
  }

  /** Getters and Setters for data access object. */
  public PlantDao getDao() {
    return dao;
  }

  public void setDao(PlantDao dao) {
    this.dao = dao;
  }
}
