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
 * Helper methods to load and clear test data from any object implementing the DatastoreOptions interface.
 */
public class LoadTestData {

  /* Datastore instance and datastore itself */
  DatastoreOptions instance;
  Datastore datastore;

  /* List of entities available at the class-level */
  private List<User> testUsers;
  private List<Garden> testGardens;
  private List<Plant> testPlants;
  private List<HasMember> testHasMembers;
  private List<ContainsPlant> testContainsPlants;

  private void loadUsers() {
    // Test User Data
    testUsers =
        Arrays.asList(
            new User("1", "ladd@example.com", "David Ladd", "My SSN is: 143-46-6098", "11201"),
            new User("2", "caroqliu@google.com", "Caroline Liu", "Plants are fun", "11201"),
            new User("3", "friedj@google.com", "Jake Fried", "Plants are fun too", "11201"));

    Batch batch = datastore.newBatch();

    for (User user : testUsers) {
      batch.add(user.toEntity());
    }

    batch.submit();
  }

  private void loadGardens() {
    // Test Garden Data
    double newYorkLat = 40.82;
    double newYorkLng = -73.93;
    testGardens =
        Arrays.asList(
            new Garden("1", "Flower Garden", newYorkLat, newYorkLng, testUsers.get(0).getId()),
            new Garden("2", "Pea Garden", newYorkLat, newYorkLng, testUsers.get(1).getId()));

    Batch batch = datastore.newBatch();

    for (Garden garden : testGardens) {
      batch.add(garden.toEntity());
    }

    batch.submit();
  }

  private void loadPlants() {
    // Test Plant Data
    testPlants =
        Arrays.asList(
            new Plant("1", "Flower Plant 1", 4, "1"),
            new Plant("2", "Flower Plant 2", 4, "2"),
            new Plant("3", "Pea Plant 1", 4, "3"),
            new Plant("4", "Pea Plant 2", 4, "4"));

    Batch batch = datastore.newBatch();

    for (Plant plant : testPlants) {
      batch.add(plant.toEntity());
    }

    batch.submit();
  }

  private void loadHasMember() {
    testHasMembers =
        Arrays.asList(
            new HasMember("1", testGardens.get(0).getId(), testUsers.get(0).getId()),
            new HasMember("2", testGardens.get(1).getId(), testUsers.get(1).getId()),
            new HasMember("3", testGardens.get(1).getId(), testUsers.get(2).getId()));

    Batch batch = datastore.newBatch();

    for (HasMember hasMember : testHasMembers) {
      batch.add(hasMember.toEntity());
    }

    batch.submit();
  }

  private void loadContainsPlant() {
    testContainsPlants =
        Arrays.asList(
            new ContainsPlant("1", testGardens.get(0).getId(), testPlants.get(0).getId()),
            new ContainsPlant("2", testGardens.get(0).getId(), testPlants.get(1).getId()),
            new ContainsPlant("3", testGardens.get(1).getId(), testPlants.get(2).getId()),
            new ContainsPlant("4", testGardens.get(1).getId(), testPlants.get(3).getId()));

    Batch batch = datastore.newBatch();

    for (ContainsPlant containsPlant : testContainsPlants) {
      batch.add(containsPlant.toEntity());
    }

    batch.submit();
  }

  /**
   * Loads test data into any DatastoreOptions object.
   *
   * @param instance The datastore instance as a DatastoreOptions object.
   */
  public void load(DatastoreOptions instance) {
    this.instance = instance;
    this.datastore = instance.getService();
    loadUsers();
    loadGardens();
    loadPlants();
    loadHasMember();
    loadContainsPlant();
  }

  /**
   * Clears all data from any Datastore instance.
   *
   * @param datastore The datastore instance as a DatastoreOptions object.
   */
  public void clear(DatastoreOptions instance) {
    this.instance = instance;
    this.datastore = instance.getService();
    
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
