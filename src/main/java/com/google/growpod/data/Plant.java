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

package com.google.growpod.data;

import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Entity.Builder;
import com.google.cloud.datastore.Key;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/** Plant data class. */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Plant {

  /** A unique id. */
  private String id;

  /** A nickname or null. */
  private String nickname;

  /** The number of plants in this plot. */
  private long count;

  /** Foreign Key to this plant's information. */
  private String plantTypeId;

  /**
   * Generates a plant from an entity.
   *
   * @param entity the entity to generate the plant from
   * @return the new plant with the entity's information.
   */
  public static Plant from(Entity entity) {
    String id = entity.getKey().getId().toString();
    String nickname = entity.getString("nickname");
    Long count = entity.getLong("count");
    String plantTypeId = entity.getString("plant-type-id");
    return new Plant(id, nickname, count, plantTypeId);
  }

  /**
   * Generates an entity from a plant.
   *
   * @param instance The datastore instance the new entity will be associated with.
   * @return the new entity representing a plant.
   */
  public Entity toEntity(DatastoreOptions instance) {
    // I use a different API here than in the portfolio
    String projectId = instance.getProjectId();
    Key key = Key.newBuilder(projectId, "Plant", Long.parseLong(id)).build();
    Builder builder = Entity.newBuilder(key);
    builder.set("nickname", nickname);
    builder.set("count", count);
    builder.set("plant-type-id", plantTypeId);
    return builder.build();
  }
}
