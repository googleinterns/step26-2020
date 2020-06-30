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

/** Plant data class. */
public class Plant {

  /** A unique id. */
  private String id;

  /** An optional nickname. */
  private String nickname;

  private long count;

  /** Foreign Key to this plant's information. */
  private String plantTypeId;

  /**
   * Constructs a new Plant.
   *
   * @param id A unique ID for the plant. This value must be supplied by the user.
   * @param nickname A nickname or null.
   * @param count The number of this type of plant.
   * @param plantTypeId The garden's administrator. THIS IS UNCHECKED.
   */
  public Plant(String id, String nickname, long count, String plantTypeId) {
    this.id = id;
    this.nickname = nickname;
    this.count = count;
    this.plantTypeId = plantTypeId;
  }

  /* Getters and setters. */
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the plant's nickname.
   *
   * @return the plant's nickname or null.
   */
  public String getNickname() {
    return nickname;
  }

  /**
   * Sets the plant's nickname.
   *
   * @param name the desired nickname or null.
   */
  public void setNickname(String name) {
    this.nickname = name;
  }

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public String getPlantTypeId() {
    return plantTypeId;
  }

  public void setPlantTypeId(String id) {
    this.plantTypeId = id;
  }
}
