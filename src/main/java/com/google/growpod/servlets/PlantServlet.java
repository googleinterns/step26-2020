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

import com.google.growpod.data.Plant;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

  /** Static test data. */
  private static final Map<String, Plant> PLANT_MAP = createPlantMap();

  private static Map<String, Plant> createPlantMap() {
    Map<String, Plant> map = new HashMap<String, Plant>();
    map.put("0", new Plant("0", "Flower Plant 1", 4, "0"));
    map.put("1", new Plant("1", "Flower Plant 2", 4, "1"));
    map.put("2", new Plant("2", "Pea Plant 1", 4, "2"));
    map.put("3", new Plant("3", "Pea Plant 2", 4, "3"));
    return Collections.unmodifiableMap(map);
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
      Plant plant = getPlantById(uriList[2]);
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

  /**
   * Retrieves a plant in the database by id, or null if said id does not exist.
   *
   * @param id the plant's id
   * @return the plant with id's data or null.
   */
  private Plant getPlantById(String id) {
    // MOCKUP IMPLEMENTATION
    return PLANT_MAP.get(id);
  }
}
