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

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.growpod.SensitiveData;
import com.google.growpod.data.User;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/** Handles internal authentication for user entities. */
public class AuthController {

  private GoogleIdTokenVerifier verifier =
      new GoogleIdTokenVerifier.Builder(
              UrlFetchTransport.getDefaultInstance(), JacksonFactory.getDefaultInstance())
          .setAudience(Collections.singletonList(SensitiveData.CLIENT_ID))
          .build();

  private DatastoreOptions datastoreInstance;
  private Datastore datastore;

  /**
   * Initializes a new authentication controller on a given datastore.
   *
   * @param datastoreInstance the database instance to run queries on.
   */
  public AuthController(DatastoreOptions datastoreInstance) {
    this.datastoreInstance = datastoreInstance;
    this.datastore = datastoreInstance.getService();
  }

  /**
   * Obtains the user's Datastore Id from a passed-in auth token.
   *
   * @param token OAuth token.
   * @return the user id or null if invalid user or ID.
   */
  public String getUserId(String token) throws IOException {
    String email = null;
    try {
      email = getUserEmail(token);
    } catch (GeneralSecurityException e) {
      return null;
    }

    if (email == null) {
      return null;
    }

    // Query id by email.
    StructuredQuery<Entity> query =
        Query.newEntityQueryBuilder()
            .setKind("User")
            .setFilter(PropertyFilter.eq("email", email))
            .build();
    QueryResults<Entity> results = datastore.run(query);
    if (!results.hasNext()) {
      return null;
    }

    Entity entity = results.next();
    User user = User.from(entity);

    return user.getId();
  }

  private String getUserEmail(String token) throws GeneralSecurityException, IOException {
    GoogleIdToken idToken = verifier.verify(token);
    if (idToken != null) {
      Payload payload = idToken.getPayload();
      return payload.getEmail();
    } else {
      return null;
    }
  }
}
