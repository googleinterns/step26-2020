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

/** User data class. */
public class User {

  /** A unique id. */
  private String id;

  /** The user's primary email. */
  private String email;

  /** This is the only name we track. */
  private String preferredName;

  /** The user's biography. */
  private String biography;

  /**
   * We do not wish to keep exact user locations -- this will store some approximate location data.
   * Unfortunately, this will only work in the US.
   */
  private String zipCode;

  /**
   * Constructs a new User.
   *
   * @param id A unique ID for the user. This value must be supplied by the user.
   * @param email The user's email.
   * @param preferredName The user's preferred name.
   * @param biography A user-submitted biography.
   * @param zipCode The user's postal code.
   */
  public User(String id, String email, String preferredName, String biography, String zipCode) {
    this.id = id;
    this.email = email;
    this.preferredName = preferredName;
    this.biography = biography;
    this.zipCode = zipCode;
  }

  /* Getters and setters. */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPreferredName() {
    return preferredName;
  }

  public void setPreferredName(String name) {
    this.preferredName = name;
  }

  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(String location) {
    this.zipCode = location;
  }
}