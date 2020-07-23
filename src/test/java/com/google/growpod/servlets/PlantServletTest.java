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

package com.google.sps;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.growpod.controllers.PlantDao;
import com.google.growpod.data.Plant;
import com.google.growpod.servlets.PlantServlet;
import com.google.gson.Gson;
import java.io.IOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/** Tests Plant servlet response behavior, based on different request URL and DAO responses. */
@ExtendWith(MockitoExtension.class)
public final class PlantServletTest {

  private PlantServlet servlet; // Class to test

  /** Mock services. */
  @Mock private PlantDao dao;

  /** Test values. */
  private final Plant TEST_PLANT = new Plant("0", "x", 0, "0");

  /** Initializes servlet object and mock dao. */
  @BeforeEach
  public void initTest() {
    servlet = new PlantServlet();
    servlet.setDao(dao);
  }

  /** Tests successful query for GET: /plant/{id} method. */
  @Test
  public void doGet_successfulPlantQuery_successfulResult() throws IOException {
    String testUrl = "/plant/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getPlantById("0")).thenReturn(TEST_PLANT);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    assertEquals(new Gson().toJson(TEST_PLANT), response.getContentAsString().trim());
  }

  /** Tests failed query for GET: /plant/{id} method. */
  @Test
  public void doGet_invalidIdPlantQuery_returns404() throws IOException {
    String testUrl = "/plant/0";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(dao.getPlantById("0")).thenReturn(null);

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_NOT_FOUND, response.getStatus());
  }

  /** Tests invalid method on GET. */
  @Test
  public void doGet_invalidUrlQuery_returns405() throws IOException {
    String testUrl = "/plant/peapod/cody-kayla-stephanie-caroline-jake";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    servlet.doGet(request, response);

    assertEquals(MockHttpServletResponse.SC_METHOD_NOT_ALLOWED, response.getStatus());
  }
}
