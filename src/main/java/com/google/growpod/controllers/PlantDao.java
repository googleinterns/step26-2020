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
import com.google.cloud.datastore.Key;
import com.google.growpod.data.Plant;

/** Data access object for Plant entities. */
public class PlantDao {

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new plant controller from a given Datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public PlantDao(DatastoreOptions datastoreInstance) {
    this.datastoreInstance = datastoreInstance;
    this.datastore = datastoreInstance.getService();
  }

  /**
   * Retrieves a plant in the database by id, or null if said id does not exist.
   *
   * @param id the plant's id
   * @return the plant with id's data or null.
   */
  public Plant getPlantById(String id) {
    String projectId = datastoreInstance.getProjectId();
    Key key = Key.newBuilder(projectId, "Plant", Long.parseLong(id)).build();
    Entity plantEntity = datastore.get(key);
    return plantEntity == null ? null : Plant.from(plantEntity);
  }
}
