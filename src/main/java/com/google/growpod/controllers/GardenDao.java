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
import com.google.growpod.data.Garden;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Data access object for Garden entities. */
public class GardenDao {

  private Datastore datastore;

  /** Static test data. */
  private static final double newYorkLat = 40.82;

  private static final double newYorkLng = -73.93;
  private static final Map<String, Garden> GARDEN_MAP = createGardenMap();

  private static Map<String, Garden> createGardenMap() {
    Map<String, Garden> map = new HashMap<String, Garden>();
    map.put("0", new Garden("0", "Flower Garden", newYorkLat, newYorkLng, "0"));
    map.put("1", new Garden("1", "Pea Garden", newYorkLat, newYorkLng, "1"));
    return Collections.unmodifiableMap(map);
  }

  private static final Map<String, List<String>> GARDEN_USER_LIST_MAP = createGardenUserListMap();

  private static Map<String, List<String>> createGardenUserListMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("0", Arrays.asList("0", "1"));
    map.put("1", Arrays.asList("0", "1", "2"));
    return Collections.unmodifiableMap(map);
  }

  private static final Map<String, List<String>> GARDEN_PLANT_LIST_MAP = createGardenPlantListMap();

  private static Map<String, List<String>> createGardenPlantListMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("0", Arrays.asList("0", "1"));
    map.put("1", Arrays.asList("2", "3"));
    return Collections.unmodifiableMap(map);
  }

  /**
   * Initializes a new garden controller from a given Datastore.
   *
   * @param datastore the database to run queries on.
   */
  public GardenDao(Datastore datastore) {
    this.datastore = datastore;
  }

  /**
   * Retrieves a garden in the database by id, or null if said id does not exist.
   *
   * @param id the garden's id
   * @return the garden with id's data or null.
   */
  public Garden getGardenById(String id) {
    // MOCK IMPLEMENTATION
    return GARDEN_MAP.get(id);
  }

  /**
   * Retrieves a list of garden members. Returns null if the garden does not exist.
   *
   * @param id the garden's id
   * @return a list of user ids in the garden or null.
   */
  public List<String> getGardenUserListById(String id) {
    // MOCK IMPLEMENTATION
    return GARDEN_USER_LIST_MAP.get(id);
  }

  /**
   * Retrieves a list of garden plants. Returns null if the garden does not exist.
   *
   * @param id the garden's id
   * @return a list of plant ids in the garden or null.
   */
  public List<String> getGardenPlantListById(String id) {
    // MOCK IMPLEMENTATION
    return GARDEN_PLANT_LIST_MAP.get(id);
  }
}
