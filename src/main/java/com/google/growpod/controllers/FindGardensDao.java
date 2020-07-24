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
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.growpod.data.Garden;
import java.util.ArrayList;
import java.util.List;

/** Data access object for searching for nearby gardens. */
public class FindGardensDao {

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new find gardens dao from a given Datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public FindGardensDao(DatastoreOptions datastoreInstance) {
    this.datastoreInstance = datastoreInstance;
    this.datastore = datastoreInstance.getService();
  }

  /**
   * Retrieves all gardens near a given zip code. Currently only returns exact matches.
   *
   * @param zipCode the specified zip code.
   * @return a list of gardens with zipcodes equal to zipCode.
   */
  public List<Garden> getNearbyGardens(String zipCode) {
    List<Garden> gardenList = new ArrayList<Garden>();

    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("Garden")
            .setFilter(PropertyFilter.eq("zip-code", zipCode))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    while (results.hasNext()) {
      Entity entity = results.next();
      Garden garden = Garden.from(entity);
      gardenList.add(garden);
    }

    return gardenList;
  }
}
