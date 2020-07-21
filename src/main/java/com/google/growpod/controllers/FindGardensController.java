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
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.growpod.data.ContainsPlant;
import com.google.growpod.data.Garden;
import com.google.growpod.data.HasMember;
import java.util.ArrayList;
import java.util.List;

/** Controller for searching for nearby gardens. */
public class FindGardensController {

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new garden controller from a given Datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public FindGardensController(DatastoreOptions datastoreInstance) {
    this.datastoreInstance = datastoreInstance;
    this.datastore = datastoreInstance.getService();
  }

  /**
   * Retrieves a garden in the database by id, or null if said id does not exist.
   *
   * @param id the garden's id
   * @return the garden with id's data or null.
   */
  public Garden getGardenById(String id) {
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Garden", Long.parseLong(id)).build();
    Entity gardenEntity = datastore.get(key);
    return gardenEntity == null ? null : Garden.from(gardenEntity);
  }

  /**
   * Retrieves a list of garden members. Returns null if the garden does not exist.
   *
   * @param id the garden's id
   * @return a list of user ids in the garden or null.
   */
  public List<String> getGardenUserListById(String id) {
    List<String> userList = new ArrayList<String>();

    // Existence check
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Garden", Long.parseLong(id)).build();
    if (datastore.get(key) == null) {
      return null;
    }

    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("HasMember")
            .setFilter(PropertyFilter.eq("garden-id", id))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      HasMember hasMember = HasMember.from(entity);
      userList.add(hasMember.getUserId());
    }

    return userList;
  }

  /**
   * Retrieves a list of garden plants. Returns null if the garden does not exist.
   *
   * @param id the garden's id
   * @return a list of plant ids in the garden or null.
   */
  public List<String> getGardenPlantListById(String id) {
    List<String> plantList = new ArrayList<String>();

    // Existence check
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Garden", Long.parseLong(id)).build();
    if (datastore.get(key) == null) {
      return null;
    }

    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("ContainsPlant")
            .setFilter(PropertyFilter.eq("garden-id", id))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      ContainsPlant containsPlant = ContainsPlant.from(entity);
      plantList.add(containsPlant.getPlantId());
    }

    return plantList;
  }
}
