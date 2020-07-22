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

import com.google.growpod.controllers.FindGardensController;
import com.google.growpod.data.Garden;
import com.google.growpod.servlets.FindGardensServlet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
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
public final class FindGardensServletTest {

  private FindGardensServlet servlet; // Class to test

  /** Mock services. */
  @Mock private FindGardensController controller;

  /** Mock values. */
  private final Garden TEST_GARDEN = new Garden("0", "x", "y", 0.0, 0.0, "0", "0");

  private final List<Garden> TEST_GARDEN_LIST = Arrays.asList(TEST_GARDEN);

  /** Initializes servlet object and mock controller. */
  @BeforeEach
  public void initTest() {
    servlet = new FindGardensServlet();
    servlet.setController(controller);
  }

  /** Tests successful handling of GET: /find-gardens (no arguments) */
  @Test
  public void testFindGardensDefault() throws IOException {
    String testUrl = "/find-gardens";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    MockHttpServletResponse response = new MockHttpServletResponse();

    // RIGHT NOW, THIS TEST RELIES ON A CONSTANT VALUE IN SOURCE CODE
    // TODO: REPLACE ONCE AUTHENTICATION IS IMPLEMENTED
    when(controller.getNearbyGardens("11201")).thenReturn(TEST_GARDEN_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    Type listType = new TypeToken<List<Garden>>() {}.getType();
    assertEquals(TEST_GARDEN_LIST, new Gson().fromJson(response.getContentAsString(), listType));
  }

  /** Tests successful handling of GET: /find-gardens?zip-code=12345 */
  @Test
  public void testFindGardensZipCode() throws IOException {
    String testUrl = "/find-gardens";

    // Mocks
    MockHttpServletRequest request = new MockHttpServletRequest("GET", testUrl);
    request.addParameter("zip-code", "12345");
    MockHttpServletResponse response = new MockHttpServletResponse();

    when(controller.getNearbyGardens("12345")).thenReturn(TEST_GARDEN_LIST);

    servlet.doGet(request, response);

    assertEquals("application/json;", response.getContentType());
    Type listType = new TypeToken<List<Garden>>() {}.getType();
    assertEquals(TEST_GARDEN_LIST, new Gson().fromJson(response.getContentAsString(), listType));
  }
}
