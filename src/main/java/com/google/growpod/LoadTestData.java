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

package com.google.growpod;

import com.google.cloud.datastore.Batch;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.growpod.data.ContainsPlant;
import com.google.growpod.data.Garden;
import com.google.growpod.data.HasMember;
import com.google.growpod.data.Plant;
import com.google.growpod.data.User;
import java.util.Arrays;
import java.util.List;

/**
 * Helper methods to load and clear test data from any object implementing the DatastoreOptions
 * interface.
 */
public class LoadTestData {

  /* Static test data */
  private static final double newYorkLat = 40.82;
  private static final double newYorkLng = -73.93;
  private static final List<User> TEST_USERS =
      Arrays.asList(
          new User("1", "ladd@example.com", "David Ladd", "My SSN is: 143-46-6098", "11201"),
          new User("2", "caroqliu@google.com", "Caroline Liu", "Plants are fun", "11201"),
          new User("3", "friedj@google.com", "Jake Fried", "Plants are fun too", "11201"));
  private static final List<Garden> TEST_GARDENS =
      Arrays.asList(
          new Garden("1", "Flower Garden", newYorkLat, newYorkLng, "1"),
          new Garden("2", "Pea Garden", newYorkLat, newYorkLng, "2"));
  private static final List<Plant> TEST_PLANTS =
      Arrays.asList(
          new Plant("1", "Flower Plant 1", 4, "1"),
          new Plant("2", "Flower Plant 2", 4, "2"),
          new Plant("3", "Pea Plant 1", 4, "3"),
          new Plant("4", "Pea Plant 2", 4, "4"));
  private static final List<HasMember> TEST_HAS_MEMBERS =
      Arrays.asList(
          new HasMember("1", "1", "1"), new HasMember("2", "2", "2"), new HasMember("3", "2", "3"));
  private static final List<ContainsPlant> TEST_CONTAINS_PLANTS =
      Arrays.asList(
          new ContainsPlant("1", "1", "1"),
          new ContainsPlant("2", "1", "2"),
          new ContainsPlant("3", "2", "3"),
          new ContainsPlant("4", "2", "4"));

  private static void loadUsers(DatastoreOptions instance) {
    Batch batch = instance.getService().newBatch();

    for (User user : TEST_USERS) {
      batch.add(user.toEntity(instance));
    }

    batch.submit();
  }

  private static void loadGardens(DatastoreOptions instance) {
    Batch batch = instance.getService().newBatch();

    for (Garden garden : TEST_GARDENS) {
      batch.add(garden.toEntity(instance));
    }

    batch.submit();
  }

  private static void loadPlants(DatastoreOptions instance) {
    Batch batch = instance.getService().newBatch();

    for (Plant plant : TEST_PLANTS) {
      batch.add(plant.toEntity(instance));
    }

    batch.submit();
  }

  private static void loadHasMembers(DatastoreOptions instance) {
    Batch batch = instance.getService().newBatch();

    for (HasMember hasMember : TEST_HAS_MEMBERS) {
      batch.add(hasMember.toEntity(instance));
    }

    batch.submit();
  }

  private static void loadContainsPlants(DatastoreOptions instance) {
    Batch batch = instance.getService().newBatch();

    for (ContainsPlant containsPlant : TEST_CONTAINS_PLANTS) {
      batch.add(containsPlant.toEntity(instance));
    }

    batch.submit();
  }

  /**
   * Loads test data into any DatastoreOptions object.
   *
   * @param instance The datastore instance as a DatastoreOptions object.
   */
  public static void load(DatastoreOptions instance) {
    loadUsers(instance);
    loadGardens(instance);
    loadPlants(instance);
    loadHasMembers(instance);
    loadContainsPlants(instance);
  }

  /**
   * Clears all data from any Datastore instance.
   *
   * @param instance The datastore instance as a DatastoreOptions object.
   */
  public static void clear(DatastoreOptions instance) {
    Datastore datastore = instance.getService();

    String[] tables = {"User", "Garden", "Plant", "HasMember", "ContainsPlant"};

    Batch batch = datastore.newBatch();

    for (String table : tables) {
      StructuredQuery<Entity> query = Query.newEntityQueryBuilder().setKind(table).build();
      QueryResults<Entity> results = datastore.run(query);
      while (results.hasNext()) {
        Key key = results.next().getKey();
        batch.delete(key);
      }
    }

    batch.submit();
  }
}
