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

package com.google.growpod.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.growpod.controllers.UserController;
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

/**
 * Tests User servlet response behavior, based on different request URL and controller responses.
 */
@ExtendWith(MockitoExtension.class)
public final class UserServletTest {

  private UserServlet servlet; // Class to test

  /** Mock services. */
  @Mock private UserController controller;

  /** Mock values. */
  private final User TEST_USER = new User("0", "x", "y", "z", "aa");

  private final List<String> TEST_GARDEN_LIST = Arrays.asList("0");

  /** Initializes servlet object and mock controller. */
  @BeforeEach
  public void initTest() {
    servlet = new UserServlet();
    servlet.setController(controller);
  }

  /** Tests successful query for GET: /user/{id} method. */
  @Test
  public void testGetUser() throws IOException {
    String testUrl = "/user/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getUserById("0")).thenReturn(TEST_USER);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(new Gson().toJson(TEST_USER), response.getContentAsString().trim());
  }

  /** Tests failed query for GET: /user/{id} method. */
  @Test
  public void testGetUserFail() throws IOException {
    String testUrl = "/user/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getUserById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /user/{id}/garden-list method. */
  @Test
  public void testGetUserGardenList() throws IOException {
    String testUrl = "/user/0/garden-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getUserGardenListById("0")).thenReturn(TEST_GARDEN_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(new Gson().toJson(TEST_GARDEN_LIST), response.getContentAsString().trim());
  }

  /** Tests failed query for GET: /user/{id}/garden-list method. */
  @Test
  public void testGetUserGardenListFail() throws IOException {
    String testUrl = "/user/0/garden-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getUserGardenListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on GET. */
  @Test
  public void testGetInvalidMethod() throws IOException {
    String testUrl = "/user/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }
}