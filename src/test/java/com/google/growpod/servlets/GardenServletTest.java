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

import com.google.growpod.controllers.GardenDao;
import com.google.growpod.data.Garden;
import com.google.growpod.data.Plant;
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

/** Tests Garden servlet response behavior, based on different request URL and DAO responses. */
@ExtendWith(MockitoExtension.class)
public final class GardenServletTest {

  private GardenServlet servlet; // Class to test

  /** Mock services. */
  @Mock private GardenDao dao;

  /** Test values. */
  private final Garden TEST_GARDEN = new Garden("0", "x", "y", 0.0, 0.0, "0", "0");

  private final Plant TEST_PLANT = new Plant("0", "x", 1, "y");
  /** Separate lists in case I change the type each query returns */
  private final List<String> TEST_USER_LIST = Arrays.asList("0");

  private final List<String> TEST_PLANT_LIST = Arrays.asList("0");

  /** Initializes servlet object and mock dao. */
  @BeforeEach
  public void initTest() {
    servlet = new GardenServlet();
    servlet.setDao(dao);
  }

  /** Tests successful query for GET: /garden/{id} method. */
  @Test
  public void doGet_successfulGardenQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenById("0")).thenReturn(TEST_GARDEN);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_GARDEN, new Gson().fromJson(response.getContentAsString(), Garden.class));
  }

  /** Tests failed query for GET: /garden/{id} method. */
  @Test
  public void doGet_invalidIdGardenQuery_returns404() throws IOException {
    String testUrl = "/garden/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /garden/{id}/user-list method. */
  @Test
  public void doGet_successfulUserListQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0/user-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenUserListById("0")).thenReturn(TEST_USER_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_USER_LIST, new Gson().fromJson(response.getContentAsString(), List.class));
  }

  /** Tests failed query for GET: /garden/{id}/user-list method. */
  @Test
  public void doGet_invalidIdUserListQuery_returns404() throws IOException {
    String testUrl = "/garden/0/user-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenUserListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for GET: /garden/{id}/plant-list method. */
  @Test
  public void doGet_successfulPlantListQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenPlantListById("0")).thenReturn(TEST_PLANT_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(TEST_PLANT_LIST, new Gson().fromJson(response.getContentAsString(), List.class));
  }

  /** Tests failed query for GET: /garden/{id}/plant-list method. */
  @Test
  public void doGet_invalidIdPlantListQuery_returns404() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenPlantListById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on GET. */
  @Test
  public void doGet_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/garden/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }

  /** Tests invalid garden id response for POST: /garden/{gid}/plant-list */
  @Test
  public void doPost_invalidGidPlantListQuery_returns404() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    request.setContent(new Gson().toJson(TEST_PLANT).getBytes());
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getGardenById("0")).thenReturn(null);

    servlet.doPost(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid request body response for POST: /garden/{gid}/plant-list */
  @Test
  public void doPost_invalidRequestBodyPlantListQuery_returns400() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    request.setContent(new Gson().toJson(TEST_PLANT).getBytes());
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.addPlant("0", TEST_PLANT)).thenReturn(null);
    when(dao.getGardenById("0")).thenReturn(TEST_GARDEN);

    servlet.doPost(request, response);

    assertEquals(MockHttpServletResponse.SC_BAD_REQUEST, response.getStatus());
  }

  /** Tests successful query for POST: /garden/{gid}/plant-list posting */
  @Test
  public void doPost_successfulPlantListQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0/plant-list";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    request.setContent(new Gson().toJson(TEST_PLANT).getBytes());
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.addPlant("0", TEST_PLANT)).thenReturn("0");
    when(dao.getGardenById("0")).thenReturn(TEST_GARDEN);

    servlet.doPost(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals("{\"id\":0}", response.getContentAsString().trim());
  }

  /** Tests invalid method on POST. */
  @Test
  public void doPost_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/garden/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("POST", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }

  /** Tests successful query for DELETE: /garden/{gid}/user-list/{uid}. */
  @Test
  public void doDelete_successfulUserListQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0/user-list/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.deleteUser("0", "0")).thenReturn(true);

    servlet.doDelete(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals("{\"id\":0}", response.getContentAsString().trim());
  }

  /** Tests failed query for DELETE: /garden/{gid}/user-list/{uid} */
  @Test
  public void doPost_invalidGidOrPidUserListQuery_returns404() throws IOException {
    String testUrl = "/garden/0/user-list/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.deleteUser("0", "0")).thenReturn(false);

    servlet.doDelete(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests successful query for DELETE: /garden/{gid}/plant-list/{pid}. */
  @Test
  public void doDelete_successfulPlantListQuery_successfulResult() throws IOException {
    String testUrl = "/garden/0/plant-list/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.deletePlant("0", "0")).thenReturn(true);

    servlet.doDelete(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals("{\"id\":0}", response.getContentAsString().trim());
  }

  /** Tests failed query for DELETE: /garden/{gid}/plant-list/{pid} */
  @Test
  public void doPost_invalidGidOrPidPlantListQuery_returns404() throws IOException {
    String testUrl = "/garden/0/plant-list/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.deletePlant("0", "0")).thenReturn(false);

    servlet.doDelete(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on DELETE. */
  @Test
  public void doDelete_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/garden/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("DELETE", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }
}
