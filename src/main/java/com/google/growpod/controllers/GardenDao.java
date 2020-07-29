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
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.growpod.data.ContainsPlant;
import com.google.growpod.data.Garden;
import com.google.growpod.data.HasMember;
import com.google.growpod.data.Plant;
import java.util.ArrayList;
import java.util.List;

/** Data access object for Garden entities. */
public class GardenDao {

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new garden controller from a given Datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public GardenDao(DatastoreOptions datastoreInstance) {
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
    Garden garden = getGardenById(id);
    if (garden == null) {
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
    Garden garden = getGardenById(id);
    if (garden == null) {
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

  /**
   * Adds a plant to a garden's plant list.
   *
   * @param gardenId the garden to add the plant to
   * @param plant the plant object
   * @return The plant's key
   */
  public String addPlant(String gardenId, Plant plant) {
    // Add plant to plant list first
    plant.setId("1"); // Dummy key to make .toEntity(datastoreInstance) work.
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Plant");
    IncompleteKey incompleteKey = keyFactory.newKey();

    Key key = datastore.allocateId(incompleteKey);

    Entity newEntity = Entity.newBuilder(key, plant.toEntity(datastoreInstance)).build();
    datastore.add(newEntity);

    // Then add relation
    keyFactory = datastore.newKeyFactory().setKind("ContainsPlant");
    incompleteKey = keyFactory.newKey();
    key = datastore.allocateId(incompleteKey);

    String plantId = Long.toString(newEntity.getKey().getId());
    ContainsPlant relation = new ContainsPlant("1", gardenId, plantId);

    newEntity = Entity.newBuilder(key, relation.toEntity(datastoreInstance)).build();
    datastore.add(newEntity);
    return plantId;
  }

  /**
   * Deletes a user id from the garden's user list.
   *
   * @param gardenId the garden's id
   * @param userId the user's id
   * @return whether the query was successful.
   */
  public boolean deleteUser(String gardenId, String userId) {
    // Existence check for key
    Garden garden = getGardenById(gardenId);
    if (garden == null) {
      return false;
    }

    // Match user and garden
    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("HasMember")
            .setFilter(PropertyFilter.eq("garden-id", gardenId))
            .setFilter(PropertyFilter.eq("user-id", userId))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    if (!results.hasNext()) {
      return false;
    }
    Entity entity = results.next();

    datastore.delete(entity.getKey());
    return true;
  }

  /**
   * Deletes a plant id, as well as a plant, from the garden's plant list.
   *
   * @param gardenId the garden's id
   * @param plantId the plant's id
   * @return whether the query was successful.
   */
  public boolean deletePlant(String gardenId, String plantId) {
    // Existence check for key
    Garden garden = getGardenById(gardenId);
    if (garden == null) {
      return false;
    }

    // Deletes relation first
    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("ContainsPlant")
            .setFilter(PropertyFilter.eq("garden-id", gardenId))
            .setFilter(PropertyFilter.eq("plant-id", plantId))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    if (!results.hasNext()) {
      return false;
    }
    Entity entity = results.next();
    datastore.delete(entity.getKey());

    // Then delete plant
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Plant", Long.parseLong(plantId)).build();
    Entity plantEntity = datastore.get(key);
    if (plantEntity == null) {
      return false;
    }
    datastore.delete(plantEntity.getKey());

    return true;
  }
}
