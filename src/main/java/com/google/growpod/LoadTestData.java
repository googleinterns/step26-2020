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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.google.cloud.datastore.*;
import com.google.growpod.data.*;

/**
 * Helper methods to load and clear test data from any object implementing the Datastore
 * interface.
 */
public class LoadTestData {

  /* List of entities available at the class-level */
  private List<User> testUsers;
  private List<Garden> testGardens;

  private void loadUsers(Datastore datastore) {
    // Test User Data
    testUsers = Arrays.asList(
      new User("0", "ladd@example.com", "David Ladd", "My SSN is: 143-46-6098", "11201"),
      new User("1", "caroqliu@google.com", "Caroline Liu", "Plants are fun", "11201"),
      new User("2", "friedj@google.com", "Jake Fried", "Plants are fun too", "11201")
    );

    Batch batch = datastore.newBatch();
    KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    // Replace keys with Datastore allocated keys and insert into batch
    for (User user : testUsers) {
      user.setId(datastore.allocateId(userKeyFactory.newKey()).toUrlSafe());
      batch.add(user.toEntity());
    }

    batch.submit();
  }

  private void loadGardens(Datastore datastore) {
    // Test Garden Data
    double newYorkLat = 40.82;
    double newYorkLng = -73.93;
    List<Garden> testGardens = Arrays.asList(
      new Garden("0", "Flower Garden", newYorkLat, newYorkLng, testUsers.get(0).getId()),
      new Garden("1", "Pea Garden", newYorkLat, newYorkLng, testUsers.get(1).getId())
    );

    Batch batch = datastore.newBatch();
    KeyFactory gardenKeyFactory = datastore.newKeyFactory().setKind("Garden");

    // Replace keys with Datastore allocated keys and insert into batch
    for (Garden garden : testGardens) {
      garden.setId(datastore.allocateId(gardenKeyFactory.newKey()).toUrlSafe());
      batch.add(garden.toEntity());
    }

    batch.submit();
  }

  private void loadPlants(Datastore datastore) {
    // Test Plant Data
    List<Plant> testPlants = Arrays.asList(
      new Plant("0", "Flower Plant 1", 4, "0"),
      new Plant("1", "Flower Plant 2", 4, "1"),
      new Plant("2", "Pea Plant 1", 4, "2"),
      new Plant("3", "Pea Plant 2", 4, "3")
    );

    Batch batch = datastore.newBatch();
    KeyFactory plantKeyFactory = datastore.newKeyFactory().setKind("Plant");

    // Replace keys with Datastore allocated keys and insert into batch
    for (Plant plant : testPlants) {
      plant.setId(datastore.allocateId(plantKeyFactory.newKey()).toUrlSafe());
      batch.add(plant.toEntity());
    }

    batch.submit();
  }

  /**
   * Loads test data into any Datastore object.
   *
   * @param datastore The datastore object.
   */
  public void load(Datastore datastore) {
    loadUsers(datastore);
    loadGardens(datastore);
    loadPlants(datastore);
  }

  /**
   * Clears all data from any Datastore object.
   *
   * @param datastore The datastore object.
   */
  public void clear(Datastore datastore) {
    Batch batch = datastore.newBatch();
    StructuredQuery<Entity> query = Query.newEntityQueryBuilder().build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Key key = results.next().getKey();
      batch.delete(key);
    }
    batch.submit();
  }
}