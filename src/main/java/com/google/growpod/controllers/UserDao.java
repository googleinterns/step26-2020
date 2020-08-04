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
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.growpod.data.Garden;
import com.google.growpod.data.HasMember;
import com.google.growpod.data.User;
import java.util.ArrayList;
import java.util.List;

/** Data access object for User entities. */
public class UserDao {

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new user controller from a given Datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public UserDao(DatastoreOptions datastoreInstance) {
    this.datastoreInstance = datastoreInstance;
    this.datastore = datastoreInstance.getService();
  }

  /**
   * Creates an Entity thats holds user data and adds it to Datastore.
   *
   * @param user User object holding user data: id,email,name,bio,zip
   */
  public void addToDatastore(User user) {
    // Generates key
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("User");
    IncompleteKey incompleteKey = keyFactory.newKey();

    Key key = datastore.allocateId(incompleteKey);

    // placeholder id
    user.setId("1");

    // Puts key into database
    Entity newEntity = Entity.newBuilder(key, user.toEntity(datastoreInstance)).build();
    datastore.add(newEntity);
  }

  /**
   * Retrieves a user in the database by id, or null if said id does not exist.
   *
   * @param id the user's id
   * @return the user with id's data or null.
   */
  public User getUserById(String id) {
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "User", Long.parseLong(id)).build();
    Entity userEntity = datastore.get(key);
    return userEntity == null ? null : User.from(userEntity);
  }

  /**
   * Retrieves a list of gardens the user with a given id is a member of. Returns an empty list if
   * the user is a member of no gardens, and null if the user does not exist.
   *
   * @param id the user's id
   * @return a list of gardens the user is a part of, or an empty list, or null.
   */
  public List<String> getUserGardenListById(String id) {
    List<String> gardenList = new ArrayList<String>();

    // Existence check
    User user = getUserById(id);
    if (user == null) {
      return null;
    }

    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("HasMember")
            .setFilter(PropertyFilter.eq("user-id", id))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      HasMember hasMember = HasMember.from(entity);
      gardenList.add(hasMember.getGardenId());
    }

    return gardenList;
  }

  /**
   * Retrieves a list of gardens the user with a given id administers. Returns an empty list if the
   * user administers no gardens, and null if the user does not exist.
   *
   * @param id the user's id
   * @return a list of gardens the user administers, or an empty list, or null.
   */
  public List<String> getUserGardenAdminListById(String id) {
    List<String> gardenList = new ArrayList<String>();

    // Existence check
    User user = getUserById(id);
    if (user == null) {
      return null;
    }

    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("Garden")
            .setFilter(PropertyFilter.eq("admin-id", id))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      Garden garden = Garden.from(entity);
      gardenList.add(garden.getId());
    }

    return gardenList;
  }

  /**
   * Record that a user has joined a garden.
   *
   * @param userId the user's id
   * @param gardenId the garden's id
   * @return whether the insert operation was successful.
   */
  public boolean addGarden(String userId, String gardenId) {
    // Existence check for both parameters.
    User user = getUserById(userId);
    if (user == null) {
      return false;
    }

    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Garden", Long.parseLong(gardenId)).build();
    Entity gardenEntity = datastore.get(key);
    if (gardenEntity == null) {
      return false;
    }

    // Then add relation
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("HasMember");
    IncompleteKey incompleteKey = keyFactory.newKey();

    key = datastore.allocateId(incompleteKey);
    HasMember relation = new HasMember("1", gardenId, userId);

    Entity newEntity = Entity.newBuilder(key, relation.toEntity(datastoreInstance)).build();
    datastore.add(newEntity);
    return true;
  }

  /**
   * Deletes a garden id from the user's garden list.
   *
   * @param userId the user's id
   * @param gardenId the garden's id
   * @return whether the query was successful.
   */
  public boolean deleteFromUserGardenList(String userId, String gardenId) {
    // Existence check for key
    User user = getUserById(userId);
    if (user == null) {
      return false;
    }

    // Match user and garden
    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("HasMember")
            .setFilter(
                CompositeFilter.and(
                    PropertyFilter.eq("garden-id", gardenId), PropertyFilter.eq("user-id", userId)))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    if (!results.hasNext()) {
      return false;
    }
    Entity entity = results.next();
    HasMember hasMember = HasMember.from(entity);

    datastore.delete(entity.getKey());
    return true;
  }
}
