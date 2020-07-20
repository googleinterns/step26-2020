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

/** Relates a garden and a user by membership. */
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class HasMember {

  /** Unique datastore id. */
  private String id;

  /** A garden id. */
  private String gardenId;

  /** A user id. */
  private String userId;

  /**
   * Generates a HasMember object from an entity.
   *
   * @param entity the entity to generate the HasMember object from
   * @return the new HasMember object with the entity's information.
   */
  public static HasMember from(Entity entity) {
    String id = entity.getKey().getId().toString();
    String gardenId = entity.getString("garden-id");
    String userId = entity.getString("user-id");
    return new HasMember(id, gardenId, userId);
  }

  /**
   * Generates an entity from a HasMember.
   *
   * @param instance The datastore instance the new entity will be associated with.
   * @return the new entity representing the HasMember relationship.
   */
  public Entity toEntity(DatastoreOptions instance) {
    // I use a different API here than in the portfolio
    String projectId = instance.getProjectId();
    Key key = Key.newBuilder(projectId, "HasMember", Long.parseLong(id)).build();
    Builder builder = Entity.newBuilder(key);
    builder.set("garden-id", gardenId);
    builder.set("user-id", userId);
    return builder.build();
  }
}
