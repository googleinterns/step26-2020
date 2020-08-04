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
import {
  HttpClient,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {OwlOptions} from 'ngx-owl-carousel-o';
import {Garden} from '../model/garden.model';
import {User} from '../model/user.model';

@Component({
  selector: 'MyGardens',
  templateUrl: './my-gardens.component.html',
  styleUrls: [
    './my-gardens.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class MyGardensComponent implements OnInit {
  isLoaded = false;

  // Garden Id List
  myGardenIdList: Array<string> | null = null;
  myGardenMap: Map<string, Garden>;
  myGardenIdListError = 'Garden list not loaded';

  // Garden Admin Id List
  myGardenAdminIdList: Array<string> | null = null;
  myGardenAdminMap: Map<string, Garden>;
  myGardenAdminIdListError = 'Garden admin list not loaded';

  // Garden admin names
  gardenAdminNames = new Map<string, string>();

  // General Error Handling
  errorMessage = '';

  /**
   * Option to display a certain number of slides based off of browser window size along with
   * some style options
   */
  customOptions: OwlOptions = {
    responsive: {
      0: {
        items: 1,
      },
      400: {
        items: 2,
      },
    },
    nav: true,
    margin: 20,
  };

  constructor(private httpClient: HttpClient) {}

  ngOnInit() {
    this.createMyGardensPage();
  }

  /**
   * Gets the list of gardens the user is a part of.
   * Returns an observable HTTP response.
   *
   * Performs GET: /user/current/garden-list
   *
   * @return the http response.
   */
  getUserGardenList(): Observable<HttpResponse<Array<string>>> {
    return this.httpClient.get<Array<string>>('/user/current/garden-list', {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Gets the list of gardens the user administers.
   * Returns an observable HTTP response.
   *
   * Performs GET: /user/current/garden-admin-list
   *
   * @return the http response.
   */
  getUserGardenAdminList(): Observable<HttpResponse<Array<string>>> {
    return this.httpClient.get<Array<string>>(
      '/user/current/garden-admin-list',
      {
        observe: 'response',
        responseType: 'json',
      }
    );
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
   * Obtains a garden's administrator name, and inserts it into
   * global set.
   *
   * @param adminId the admin id.
   */
  getGardenAdminName(adminId: string) {
    this.gardenAdminNames.set(adminId, 'Loading...');
    // Obtain user names
    this.getUserInfo(adminId).subscribe({
      next: (response: HttpResponse<User>) => {
        // Successful responses are handled here.
        this.gardenAdminNames.set(adminId, response.body.preferredName);
      },
      error: (error: HttpErrorResponse) => {
        // Handle connection error
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          this.gardenAdminNames.set(adminId, 'Cannot fetch name');
          return;
        }
        console.error('Unexpected error: ' + error.statusText);
        this.gardenAdminNames.set(adminId, 'Cannot fetch name');
      },
    });
  }

  /**
   * Deletes a garden from a user's garden list.
   *
   * Performs DELETE: /user/current/garden-list/{id}
   *
   * @param id the garden id to delete.
   * @return the http response.
   */
  deleteFromUserGardenList(id: string): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>('/user/current/garden-list/' + id, {
      observe: 'response',
      responseType: 'json',
    });
  }

  /**
   * Constructs a user garden admin list.
   * Step 1 in construction.
   *
   */
  createUserGardenAdminList(): void {
    this.getUserGardenAdminList().subscribe({
      next: (response: HttpResponse<Array<string>>) => {
        // Successful responses are handled here.
        this.myGardenAdminIdList = response.body;
        // Gets user names
        this.myGardenAdminMap = new Map<string, Garden>();
        // Initiates next step
        this.createUserGardenList();
        this.myGardenAdminIdList.forEach(id => {
          this.getGardenInfo(id).subscribe({
            // Obtain user names
            next: (response: HttpResponse<Garden>) => {
              // Successful responses are handled here.
              this.myGardenAdminMap.set(id, response.body);
              this.getGardenAdminName(response.body.adminId);
            },
            error: (error: HttpErrorResponse) => {
              // Handle connection error
              if (error.error instanceof ErrorEvent) {
                console.error('Network error: ' + error.error.message);
                return;
              }
              console.error('Unexpected error: ' + error.statusText);
            },
          });
        });
      },
      error: (error: HttpErrorResponse) => {
        this.myGardenAdminIdList = null;
        if (error.error instanceof ErrorEvent) {
          // Handle connection error
          console.error('Network error: ' + error.error.message);
          this.myGardenAdminIdListError = 'Cannot connect to GrowPod Server';
          return;
        } if (error.status !== 404) {
          // Non-404 error codes
          console.error('Unexpected error: ' + error.statusText);
          this.myGardenAdminIdListError =
            'Unexpected error ' + error.status + ': ' + error.statusText;
        } else {
          console.error('Error ' + error.status + ': ' + error.statusText);
          this.myGardenAdminIdListError =
            'Cannot see garden admin list for current user';
        }
        this.isLoaded = true;
      },
    });
  }

  /**
   * Constructs a user garden list.
   * Step 2 in construction.
   *
   */
  createUserGardenList(): void {
    this.getUserGardenList().subscribe({
      next: (response: HttpResponse<Array<string>>) => {
        // Successful responses are handled here.
        this.myGardenIdList = response.body;
        // Gets user names
        this.myGardenMap = new Map<string, Garden>();
        // Initiates Final Step
        this.filterAdminGardens();
        this.isLoaded = true;
        this.myGardenIdList.forEach(id => {
          this.getGardenInfo(id).subscribe({
            // Obtain user names
            next: (response: HttpResponse<Garden>) => {
              // Successful responses are handled here.
              this.myGardenMap.set(id, response.body);
              this.getGardenAdminName(response.body.adminId);
            },
            error: (error: HttpErrorResponse) => {
              // Handle connection error
              if (error.error instanceof ErrorEvent) {
                console.error('Network error: ' + error.error.message);
                return;
              }
              console.error('Unexpected error: ' + error.statusText);
            },
          });
        });
      },
      error: (error: HttpErrorResponse) => {
        this.myGardenIdList = null;
        if (error.error instanceof ErrorEvent) {
          // Handle connection error
          console.error('Network error: ' + error.error.message);
          this.myGardenIdListError = 'Cannot connect to GrowPod Server';
          return;
        } if (error.status !== 404) {
          // Non-404 error codes
          console.error('Unexpected error: ' + error.statusText);
          this.myGardenIdListError =
            'Unexpected error ' + error.status + ': ' + error.statusText;
        } else {
          console.error('Error ' + error.status + ': ' + error.statusText);
          this.myGardenIdListError =
            'Cannot see garden list for current user';
        }
        this.isLoaded = true;
      },
    });
  }

  /**
   * Filters admin gardens from user garden list.
   * Step 3 in construction.
   *
   * Filters out admin gardens in O(n + m) time, where
   * n = no. total gardens and m is no. admin gardens.
   */
  filterAdminGardens(): void {
    const adminSet = new Set<string>(this.myGardenAdminIdList); // O(m)
    this.myGardenIdList = this.myGardenIdList.filter(id => !adminSet.has(id)); // O(n)
  }

  /**
   * Create the my-gardens page in its entirety.
   * This requires a trickier implementation due to the
   * incongruency between this page's design and the backend server design.
   *
   * The page displays two carousels, one containing gardens the user has joined
   * and the other containing gardens the user has administered. However, the backend
   * API defines two lists, garden-list, and garden-admin-list, and the gardens in
   * garden-admin-list will also be in garden-list.
   *
   * Thus, we need a way of preventing gardens from being redundantly displayed.
   * To do this, I implement a series of functions that call each other sequentially.
   * I do not sequence the functions directly in createMyGardensPage() as each of these
   * actions is asynchronous.
   *
   * The sequence of functions called is as follows:
   * 1. createUserGardenAdminList
   * 2. createUserGardenList
   * 3. filterAdminGardens
   *
   */
  createMyGardensPage(): void {
    this.createUserGardenAdminList();
  }

  /**
   * Allows users to leave a garden they are a part of.
   *
   * @param id the id of the garden to leave.
   */
  leaveGarden(id: string): void {
    this.deleteFromUserGardenList(id).subscribe({
      next: () => {
        this.createMyGardensPage();
      },
      error: (error: HttpErrorResponse) => {
        // Do nothing visible for errors, yet
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          return;
        }
        console.error('Unexpected error: ' + error.statusText);
      },
    });
  }
}
