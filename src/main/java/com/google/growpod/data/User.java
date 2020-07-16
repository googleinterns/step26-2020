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

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Entity.Builder;
import com.google.cloud.datastore.Key;
import lombok.Data;

/** User data class. */
@Data
public class User {

  /** A unique id. */
  final private String id;

  /** The user's primary email. */
  final private String email;

  /** This is the only name we track. */
  final private String preferredName;

  /** The user's biography. */
  final private String biography;

  /**
   * We do not wish to keep exact user locations -- this will store some approximate location data.
   * Unfortunately, this will only work in the US.
   */
  final private String zipCode;

  /**
   * Generates a user from an entity.
   *
   * @param entity the entity to generate the user from
   * @return the new user with the entity's information.
   */
  public static User from(Entity entity) {
    String id = entity.getKey().toUrlSafe();
    String email = entity.getString("email");
    String preferredName = entity.getString("preferred-name");
    String biography = entity.getString("biography");
    String zipCode = entity.getString("zip-code");
    return new User(id, email, preferredName, biography, zipCode);
  }

  /**
   * Generates an entity from a user.
   *
   * @return the new entity representing a user.
   */
  public Entity toEntity() {
    // I use a different API here than in the portfolio
    Builder builder = Entity.newBuilder(Key.fromUrlSafe(id));
    builder.set("email", email);
    builder.set("preferred-name", preferredName);
    builder.set("biography", biography);
    builder.set("zip-code", zipCode);
    return builder.build();
  }
}
