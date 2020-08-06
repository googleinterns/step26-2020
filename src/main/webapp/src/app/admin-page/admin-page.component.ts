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
import {OAuthSession} from '../../sessions/oauth.session';
import {ActivatedRoute} from '@angular/router';
import {
  HttpClient,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import {Observable} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';
import {AddPlantComponent} from '../add-plant-form/add-plant-form.component';
import {PlantModalComponent} from '../plant-modal/plant-modal.component';
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
  isLoaded = false;
  gardenProfile: Garden | null = null;
  gardenManager: string;

  // User list
  gardenUserIdList: Array<string> | null = null;
  gardenUserNameMap: Map<string, string>;
  gardenUserIdListError = 'User list not loaded';

  // Plant list
  gardenPlantIdList: Array<string> | null = null;
  gardenPlantNameMap: Map<string, string>;
  gardenPlantIdListError = 'Plant list not loaded';

  // General Error Handling
  errorMessage = '';
  oAuthToken: string;

  /**
   * Initializes the component based on provided arguments
   *
   * @param route Contains arguments.
   */
  constructor(
    private authService: OAuthSession,
    private route: ActivatedRoute,
    private httpClient: HttpClient,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.oAuthToken = this.authService.userData.idToken;
    const gardenIdArg = this.route.snapshot.paramMap.get('garden-id');
    if (!gardenIdArg) {
      this.errorMessage = 'No garden-id argument in the query string.';
      this.isLoaded = true;
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
      params: {
        token: this.oAuthToken,
      },
    });
  }

  /**
   * Gets all users this garden contains.
   *
   * Performs GET: /garden/{garden}/user-list
   *
   * @return the http response.
   */
  getGardenUserList(garden: string): Observable<HttpResponse<Array<String>>> {
    return this.httpClient.get<Array<String>>(
      '/garden/' + garden + '/user-list',
      {
        observe: 'response',
        responseType: 'json',
        params: {
          token: this.oAuthToken,
        },
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
  getGardenPlantList(garden: string): Observable<HttpResponse<Array<String>>> {
    return this.httpClient.get<Array<String>>(
      '/garden/' + garden + '/plant-list',
      {
        observe: 'response',
        responseType: 'json',
        params: {
          token: this.oAuthToken,
        },
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
      params: {
        token: this.oAuthToken,
      },
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
      params: {
        token: this.oAuthToken,
      },
    });
  }

  /**
   * Posts a plant to a garden.
   *
   * Performs POST: /garden/{{gardenProfile.id}}/plant-list, with
   * body plant.
   * TODO validate login
   *
   * @param plant the plant to post.
   * @return the http response.
   */
  postPlantToGarden(plant: Plant): Observable<HttpResponse<string>> {
    return this.httpClient.post<string>(
      '/garden/' + this.gardenProfile.id + '/plant-list',
      plant,
      {
        observe: 'response',
        responseType: 'json',
        params: {
          token: this.oAuthToken,
        },
      }
    );
  }

  /**
   * Deletes a plant from a garden.
   *
   * Performs GET: /garden/{{gardenProfile.id}}/plant-list/{id}
   * TODO validate login
   *
   * @param id the plant id to delete.
   * @return the http response.
   */
  deletePlant(id: string): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>(
      '/garden/' + this.gardenProfile.id + '/plant-list/' + id,
      {
        observe: 'response',
        responseType: 'json',
        params: {
          token: this.oAuthToken,
        },
      }
    );
  }

  /**
   * Deletes a user from a garden.
   *
   * Performs GET: /garden/{{gardenProfile.id}}/user-list/{id}
   * TODO validate login
   *
   * @param id the user id to delete.
   * @return the http response.
   */
  deleteUser(id: string): Observable<HttpResponse<string>> {
    return this.httpClient.delete<string>(
      '/garden/' + this.gardenProfile.id + '/user-list/' + id,
      {
        observe: 'response',
        responseType: 'json',
        params: {
          token: this.oAuthToken,
        },
      }
    );
  }

  /**
   * Creates garden summary on the page.
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
        this.gardenManager = 'Loading';
        this.getUserInfo(this.gardenProfile.adminId).subscribe({
          next: (response: HttpResponse<User>) => {
            this.gardenManager = response.body.preferredName;
          },
          error: (error: HttpErrorResponse) => {
            // Handle connection error
            if (error.error instanceof ErrorEvent) {
              console.error('Network error: ' + error.error.message);
              this.gardenManager = 'Cannot fetch name';
              return;
            }
            console.error('Unexpected error: ' + error.statusText);
            this.gardenManager = 'Cannot fetch name';
          },
        });
      },
      error: (error: HttpErrorResponse) => {
        this.gardenProfile = null;
        if (error.error instanceof ErrorEvent) {
          // Connection Error
          console.error('Network error: ' + error.error.message);
          this.errorMessage = 'Cannot connect to GrowPod Server';
        } else if (error.status !== 404) {
          // Non-404 statuses
          console.error('Unexpected error: ' + error.statusText);
          this.errorMessage =
            'Unexpected error ' + error.status + ': ' + error.statusText;
        } else {
          console.error('Error ' + error.status + ': ' + error.statusText);
          this.errorMessage =
            'Cannot see garden profile for garden id: ' + garden;
        }
        this.isLoaded = true;
      },
    });
  }

  /**
   * Creates garden user list on the page.
   *
   * @param garden The garden to create a user list of.
   */
  createGardenUserList(garden: string): void {
    this.getGardenUserList(garden).subscribe({
      next: (response: HttpResponse<Array<string>>) => {
        // Successful responses are handled here.
        this.gardenUserIdList = response.body;
        // Gets user names
        this.gardenUserNameMap = new Map<string, string>();
        this.gardenUserIdList.forEach(id => {
          this.gardenUserNameMap.set(id, 'Loading...');
          this.getUserInfo(id).subscribe({
            // Obtain user names
            next: (response: HttpResponse<User>) => {
              // Successful responses are handled here.
              this.gardenUserNameMap.set(id, response.body.preferredName);
            },
            error: (error: HttpErrorResponse) => {
              // Handle connection error
              if (error.error instanceof ErrorEvent) {
                console.error('Network error: ' + error.error.message);
                this.gardenUserNameMap.set(id, 'Cannot fetch name');
                return;
              }
              console.error('Unexpected error: ' + error.statusText);
              this.gardenUserNameMap.set(id, 'Cannot fetch name');
            },
          });
        });
      },
      error: (error: HttpErrorResponse) => {
        this.gardenUserIdList = null;
        if (error.error instanceof ErrorEvent) {
          // Handle connection error
          console.error('Network error: ' + error.error.message);
          this.gardenUserIdListError = 'Cannot connect to GrowPod Server';
        } else if (error.status !== 404) {
          // Non-404 errors
          console.error('Unexpected error: ' + error.statusText);
          this.gardenUserIdListError =
            'Unexpected error ' + error.status + ': ' + error.statusText;
        } else {
          console.error('Error ' + error.status + ': ' + error.statusText);
          this.gardenUserIdListError =
            'Cannot see user list for garden id: ' + garden;
        }
      },
    });
  }

  /**
   * Creates garden plant list on the page.
   *
   * @param garden The garden to create a plant list of.
   */
  createGardenPlantList(garden: string): void {
    this.getGardenPlantList(garden).subscribe({
      next: (response: HttpResponse<Array<string>>) => {
        // Successful responses are handled here.
        this.gardenPlantIdList = response.body;
        // Get plant names
        this.gardenPlantNameMap = new Map<string, string>();
        this.gardenPlantIdList.forEach(id => {
          this.gardenPlantNameMap.set(id, 'Loading...');
          this.getPlantInfo(id).subscribe({
            // Obtain plant names
            next: (response: HttpResponse<Plant>) => {
              // Successful responses are handled here.
              this.gardenPlantNameMap.set(id, response.body.nickname);
            },
            error: (error: HttpErrorResponse) => {
              // Handle connection error
              if (error.error instanceof ErrorEvent) {
                console.error('Network error: ' + error.error.message);
                this.gardenPlantNameMap.set(id, 'Cannot fetch name');
                return;
              }
              console.error('Unexpected error: ' + error.statusText);
              this.gardenPlantNameMap.set(id, 'Cannot fetch name');
            },
          });
        });
      },
      error: (error: HttpErrorResponse) => {
        this.gardenPlantIdList = null;
        if (error.error instanceof ErrorEvent) {
          // Connection error
          console.error('Network error: ' + error.error.message);
          this.gardenPlantIdListError = 'Cannot connect to GrowPod Server';
        }
        if (error.status !== 404) {
          // Non-404 errors
          console.error('Unexpected error: ' + error.statusText);
          this.gardenPlantIdListError =
            'Unexpected error ' + error.status + ': ' + error.statusText;
        } else {
          console.error('Error ' + error.status + ': ' + error.statusText);
          this.gardenPlantIdListError =
            'Cannot see plant list for garden id: ' + garden;
        }
      },
    });
  }

  /**
   * Creates garden administration page based on the given garden id.
   *
   * @param garden The garden to create an admin page of.
   */
  createAdminPage(garden: string): void {
    this.createGardenSummary(garden);
  }

  /**
   * Opens a modal containing more information about a particular plant.
   *
   * @param id the plant id to show more information about.
   */
  showPlantDetails(id: string) {
    this.getPlantInfo(id).subscribe({
      // Obtain plant names
      next: (response: HttpResponse<Plant>) => {
        // Successful responses are handled here.
        this.dialog.open(PlantModalComponent, {
          data: response.body,
        });
      },
      error: (error: HttpErrorResponse) => {
        // Do nothing visible for errors yet
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          return;
        }
        console.error('Unexpected error: ' + error.statusText);
      },
    });
  }

  /**
   * Shows a modal to add a plant to the garden, then adds it.
   *
   */
  addPlant(): void {
    const inputModal = this.dialog.open(AddPlantComponent);
    inputModal.afterClosed().subscribe((plant: Plant | undefined) => {
      if (plant) {
        this.postPlantToGarden(plant).subscribe({
          next: () => {
            this.createGardenPlantList(this.gardenProfile.id);
          },
          error: AdminPageComponent.logError,
        });
      }
    });
  }

  /**
   * Removes a plant from a garden.
   *
   * @param id The plant id to delete.
   */
  removePlant(id: string): void {
    this.deletePlant(id).subscribe({
      next: () => {
        this.createGardenPlantList(this.gardenProfile.id);
      },
      error: AdminPageComponent.logError,
    });
  }

  /**
   * Removes a user from a garden.
   *
   * @param id The user id to delete.
   */
  removeUser(id: string): void {
    this.deleteUser(id).subscribe({
      next: () => {
        this.createGardenUserList(this.gardenProfile.id);
      },
      error: AdminPageComponent.logError,
    });
  }

  /**
   * Simply logs HTTP error responses in the console.
   *
   * @param error the http error to log
   */
  static logError(error: HttpErrorResponse) {
    // Do nothing visible for errors, yet
    if (error.error instanceof ErrorEvent) {
      console.error('Network error: ' + error.error.message);
      return;
    }
    console.error('Unexpected error: ' + error.statusText);
  }
}
