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

package com.google.growpod.controllers;

import com.google.cloud.datastore.Datastore;
import com.google.growpod.data.Plant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** Controller for Plant entities. */
public class PlantController {

  private Datastore datastore;

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
   * Initializes a new plant controller from a given Datastore.
   *
   * @param datastore the database to run queries on.
   */
  public PlantController(Datastore datastore) {
    this.datastore = datastore;
  }

  /**
   * Retrieves a plant in the database by id, or null if said id does not exist.
   *
   * @param id the plant's id
   * @return the plant with id's data or null.
   */
  public Plant getPlantById(String id) {
    // MOCK IMPLEMENTATION
    return PLANT_MAP.get(id);
  }
}
