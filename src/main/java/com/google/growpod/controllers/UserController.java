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

package com.google.growpod.controllers;

import com.google.cloud.datastore.Datastore;
import com.google.growpod.data.User;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Controller for User entities. */
public class UserController {

  private Datastore datastore;

  /** Static test data. */
  private static final String CURRENT_USER_KEY = "0";

  private static final Map<String, User> USER_MAP = createUserMap();

  private static Map<String, User> createUserMap() {
    Map<String, User> map = new HashMap<String, User>();
    map.put(
        "0", new User("0", "ladd@example.com", "David Ladd", "My SSN is: 143-46-6098", "11201"));
    map.put("1", new User("1", "caroqliu@google.com", "Caroline Liu", "Plants are fun", "11201"));
    map.put("2", new User("2", "friedj@google.com", "Jake Fried", "Plants are fun too", "11201"));
    return Collections.unmodifiableMap(map);
  }

  private static final Map<String, List<String>> USER_GARDEN_LIST_MAP = createUserGardenListMap();

  private static Map<String, List<String>> createUserGardenListMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("0", Arrays.asList("0", "1"));
    map.put("1", Arrays.asList("1"));
    map.put("2", Arrays.asList("1"));
    return Collections.unmodifiableMap(map);
  }

  private static final Map<String, List<String>> USER_GARDEN_ADMIN_LIST_MAP =
      createUserGardenAdminListMap();

  private static Map<String, List<String>> createUserGardenAdminListMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("0", Arrays.asList("0"));
    map.put("1", Arrays.asList("1"));
    map.put("2", Arrays.asList());
    return Collections.unmodifiableMap(map);
  }

  /**
   * Initializes a new user controller from a given Datastore.
   *
   * @param datastore the database to run queries on.
   */
  public UserController(Datastore datastore) {
    this.datastore = datastore;
  }

  /**
   * Retrieves a user in the database by id, or null if said id does not exist.
   *
   * @param id the user's id
   * @return the user with id's data or null.
   */
  public User getUserById(String id) {
    // MOCK IMPLEMENTATION
    return USER_MAP.get(id);
  }

  /**
   * Retrieves a list of gardens the user with a given id is a member of. Returns an empty list if
   * the user is a member of no gardens, and null if the user does not exist.
   *
   * @param id the user's id
   * @return a list of gardens the user is a part of, or an empty list, or null.
   */
  public List<String> getUserGardenListById(String id) {
    // MOCK IMPLEMENTATION
    return USER_GARDEN_LIST_MAP.get(id);
  }

  /**
   * Retrieves a list of gardens the user with a given id administers. Returns an empty list if the
   * user administers no gardens, and null if the user does not exist.
   *
   * @param id the user's id
   * @return a list of gardens the user administers, or an empty list, or null.
   */
  public List<String> getUserGardenAdminListById(String id) {
    // MOCK IMPLEMENTATION
    return USER_GARDEN_ADMIN_LIST_MAP.get(id);
  }
}
