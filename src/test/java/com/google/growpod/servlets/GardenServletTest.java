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

import com.google.growpod.controllers.GardenController;
import com.google.growpod.data.Garden;
import com.google.growpod.servlets.GardenServlet;
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
 * Tests Garden servlet response behavior, based on different request URL and controller responses.
 */
@ExtendWith(MockitoExtension.class)
public final class GardenServletTest {

  private GardenServlet servlet; // Class to test

  /** Mock services. */
  @Mock private GardenController controller;

  /** Mock values. */
  private final Garden TEST_GARDEN = new Garden("0", "x", 0.0, 0.0, "0");
  /** Separate lists in case I change the type each query returns */
  private final List<String> TEST_USER_LIST = Arrays.asList("0");

  private final List<String> TEST_PLANT_LIST = Arrays.asList("0");

  /** Initializes servlet object and mock controller. */
  @BeforeEach
  public void initTest() {
    servlet = new GardenServlet();
    servlet.setController(controller);
  }

  /** Tests successful query for GET: /garden/{id} method. */
  @Test
  public void testGetGarden() throws IOException {
    String testUrl = "/garden/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenById("0")).thenReturn(TEST_GARDEN);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_GARDEN, new Gson().fromJson(response.getContentAsString(), Garden.class));
  }

  /** Tests failed query for GET: /garden/{id} method. */
  @Test
  public void testGetGardenFail() throws IOException {
    String testUrl = "/garden/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /garden/{id}/user-list method. */
  @Test
  public void testGetGardenUserList() throws IOException {
    String testUrl = "/garden/0/user-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenUserListById("0")).thenReturn(TEST_USER_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_USER_LIST, new Gson().fromJson(response.getContentAsString(), List.class));
  }

  /** Tests failed query for GET: /garden/{id}/user-list method. */
  @Test
  public void testGetGardenUserListFail() throws IOException {
    String testUrl = "/garden/0/user-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenUserListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /garden/{id}/plant-list method. */
  @Test
  public void testGetGardenPlantList() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenPlantListById("0")).thenReturn(TEST_PLANT_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_PLANT_LIST, new Gson().fromJson(response.getContentAsString(), List.class));
  }

  /** Tests failed query for GET: /garden/{id}/plant-list method. */
  @Test
  public void testGetGardenPlantListFail() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getGardenPlantListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on GET. */
  @Test
  public void testGetInvalidMethod() throws IOException {
    String testUrl = "/garden/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }
}
