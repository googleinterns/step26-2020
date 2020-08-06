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
import com.google.growpod.controllers.AuthController;
import com.google.growpod.controllers.FindGardensDao;
import com.google.growpod.controllers.UserDao;
import com.google.growpod.data.Garden;
import com.google.growpod.data.User;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.security.GeneralSecurityException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that returns gardens close to either a given ZIP code or the logged-in user's ZIP code,
 * upon a GET request.
 */
@WebServlet({"/find-gardens"})
public class FindGardensServlet extends HttpServlet {

  static final long serialVersionUID = 1L;

  private FindGardensDao dao;
  private UserDao userDao;
  private AuthController auth;

  /** Initializes the servlet. Connects it to Datastore. */
  @Override
  public void init() throws ServletException {
    DatastoreOptions datastoreInstance = DatastoreOptions.getDefaultInstance();
    this.dao = new FindGardensDao(datastoreInstance);
    this.userDao = new UserDao(datastoreInstance);
    this.auth = new AuthController(datastoreInstance);
  }

  /**
   * Processes HTTP GET requests for the /find-gardens servlet. The optional argument `zip-code` can
   * specify where to look for gardens, otherwise, the user's zip code suffices.
   *
   * @param request Information about the GET Request
   * @param response Information about the servlet's response
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String zipCode = request.getParameter("zip-code");
    if (zipCode == null) {
      String token = request.getParameter("token");
      if (token == null) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No OAuth Key");
        return;
      }
      String userId = null;
      try {
        userId = auth.getUserId(token);
      } catch (GeneralSecurityException e) {
        userId = null;
      }
      if (userId == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization failure");
        return;
      }

      User user = userDao.getUserById(userId);
      zipCode = user.getZipCode();
    }

    List<Garden> nearbyGardens = dao.getNearbyGardens(zipCode);

    response.setContentType("application/json;");
    response.getWriter().println(new Gson().toJson(nearbyGardens));
  }

  /** Getters and Setters for data access object. */
  public FindGardensDao getDao() {
    return dao;
  }

  public void setDao(FindGardensDao dao) {
    this.dao = dao;
  }
}
