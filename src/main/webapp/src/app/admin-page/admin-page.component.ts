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

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {
  HttpClient,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Garden} from '../model/garden.model';
import {Plant} from '../model/plant.model';
import {User} from '../model/user.model';

@Component({
  selector: 'admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: [
    './admin-page.component.css',
    '../common/growpod-page-styles.css',
  ],
})

/**
 * This is a component designed to allow administrators to manage the gardens they
 * own.
 *
 * This page takes an argument 'garden-id'. This is not optional, and determines
 * the garden that is being administered. Only the garden owner can make changes to
 * their garden.
 */
export class AdminPageComponent implements OnInit {
  isLoaded = true;
  gardenProfile: Garden | null;
  gardenUserIdList: Array<string> | null = null;
  gardenUserList: Array<User>;
  gardenPlantIdList: Array<string> | null = null;
  gardenPlantList: Array<Plant>;
  errorMessage = '';

  /**
   * Initializes the component based on provided arguments
   *
   * @param route Contains arguments.
   */
  constructor(private route: ActivatedRoute, private httpClient: HttpClient) {}

  ngOnInit(): void {
    const gardenIdArg = this.route.snapshot.paramMap.get('garden-id');
    if (!gardenIdArg) {
      this.gardenProfile = null;
      this.errorMessage = 'No garden-id argument in the query string.';
      return;
    }
    this.createAdminPage(gardenIdArg);
  }

  /**
   * Gets garden information for the specified garden from
   * the server. Returns an observable HTTP response.
   *
   * Performs GET: /garden/{garden}
   *
   * @param garden The garden reqested from the server.
   * @return the http response.
   */
  getGardenInfo(garden: string): Observable<HttpResponse<Garden>> {
    return this.httpClient.get<Garden>('/garden/' + garden, {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Gets all users this garden contains.
   *
   * Performs GET: /garden/{garden}/user-list
   *
   * @return the http response.
   */
  getCurrentGardenUserList(
    garden: string
  ): Observable<HttpResponse<Array<String>>> {
    return this.httpClient.get<Array<String>>(
      '/garden/' + garden + '/user-list',
      {
        observe: 'response',
        responseType: 'json',
      }
    );
  }

  /**
   * Gets all plants this garden contains.
   *
   * Performs GET: /garden/{garden}/plant-list
   *
   * @return the http response.
   */
  getCurrentGardenPlantList(
    garden: string
  ): Observable<HttpResponse<Array<String>>> {
    return this.httpClient.get<Array<String>>(
      '/garden/' + garden + '/plant-list',
      {
        observe: 'response',
        responseType: 'json',
      }
    );
  }

  /**
   * Gets user information for the specified user from
   * the server. Returns an observable HTTP response.
   *
   * Performs GET: /user/{user}
   *
   * @param user The user reqested from the server.
   * @return the http response.
   */
  getUserInfo(user: string): Observable<HttpResponse<User>> {
    return this.httpClient.get<User>('/user/' + user, {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Gets plant information for the specified plant from
   * the server. Returns an observable HTTP response.
   *
   * Performs GET: /plant/{plant}
   *
   * @param plant The plant reqested from the server.
   * @return the http response.
   */
  getPlantInfo(plant: string): Observable<HttpResponse<Plant>> {
    return this.httpClient.get<Plant>('/plant/' + plant, {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Creates garden summary.
   *
   * @param garden The garden to create an admin page of.
   */
  createGardenSummary(garden: string): void {
    this.getGardenInfo(garden).subscribe({
      next: (response: HttpResponse<Garden>) => {
        // Successful responses are handled here.
        this.gardenProfile = response.body;
        this.isLoaded = true;
        // Get rest of information
        this.createGardenUserList(garden);
        this.createGardenPlantList(garden);
      },
      error: (error: HttpErrorResponse) => {
        // Handle connection error
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          this.gardenProfile = null;
          this.errorMessage = 'Cannot connect to GrowPod Server';
          this.isLoaded = true;
          return;
        }
        // Non-404 error codes
        if (error.status !== 404) {
          console.error('Unexpected error: ' + error.statusText);
          this.gardenProfile = null;
          this.errorMessage =
            'Unexpected error ' + error.status + ': ' + error.statusText;
          this.isLoaded = true;
          return;
        }
        console.error('Error ' + error.status + ': ' + error.statusText);
        this.gardenProfile = null;
        this.errorMessage = 'Cannot see garden profile for user id: ' + garden;
        this.isLoaded = true;
      },
    });
  }

  /**
   * Creates garden user list.
   *
   * @param garden The garden to create a user list of.
   */
  createGardenUserList(garden: string): void {}

  /**
   * Creates garden plant list.
   *
   * @param garden The garden to create a plant list of.
   */
  createGardenPlantList(garden: string): void {}

  /**
   * Creates garden administration page based on the given garden id.
   *
   * @param garden The garden to create an admin page of.
   */
  createAdminPage(garden: string): void {
    this.createGardenSummary(garden);
  }
}
