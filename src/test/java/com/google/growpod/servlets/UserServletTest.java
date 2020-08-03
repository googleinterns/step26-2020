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

package com.google.growpod.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.growpod.controllers.UserDao;
import com.google.growpod.data.User;
import com.google.growpod.servlets.UserServlet;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/** Tests User servlet response behavior, based on different request URL and DAO responses. */
@ExtendWith(MockitoExtension.class)
public final class UserServletTest {

  private UserServlet servlet; // Class to test

  /** Mock services. */
  @Mock private UserDao dao;

  /** Test values. */
  private final User TEST_USER = new User("0", "x", "y", "z", "aa");

  private final List<String> TEST_GARDEN_LIST = Arrays.asList("0");

  /** Initializes servlet object and mock dao. */
  @BeforeEach
  public void initTest() {
    servlet = new UserServlet();
    servlet.setDao(dao);
  }

  /** Tests successful query for GET: /user/{id} method. */
  @Test
  public void doGet_successfulUserQuery_successfulResult() throws IOException {
    String testUrl = "/user/0";

    // Mocks

    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getUserById("0")).thenReturn(TEST_USER);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_USER, new Gson().fromJson(response.getContentAsString(), User.class));
  }

  /** Tests failed query for GET: /user/{id} method. */
  @Test
  public void doGet_invalidIdUserQuery_returns404() throws IOException {
    String testUrl = "/user/0";

    // Mocks

    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getUserById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /user/{id}/garden-list method. */
  @Test
  public void doGet_successfulGardenListQuery_successfulResult() throws IOException {
    String testUrl = "/user/0/garden-list";

    // Mocks

    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getUserGardenListById("0")).thenReturn(TEST_GARDEN_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_GARDEN_LIST, new Gson().fromJson(response.getContentAsString(), List.class));
  }

  /** Tests failed query for GET: /user/{id}/garden-list method. */
  @Test
  public void doGet_invalidIdGardenListQuery_returns404() throws IOException {
    String testUrl = "/user/0/garden-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getUserGardenListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on GET. */
  @Test
  public void doGet_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/user/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks

    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }

  /** Tests successful query for POST: /user/{uid}/garden-list/{gid} posting */
  @Test
  public void doPost_successfulPlantListQuery_successfulResult() throws IOException {
    String testUrl = "/user/0/garden-list/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.addToUserGardenList("0", "0")).thenReturn(true);

    servlet.doPost(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals("{\"id\":0}", response.getContentAsString().trim());
  }

  /** Tests invalid method on POST. */
  @Test
  public void doPost_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/user/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }
}
