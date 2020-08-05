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
import {FormControl, Validators} from '@angular/forms';
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
  selector: 'FindGardens',
  templateUrl: './find-gardens.component.html',
  styleUrls: [
    './find-gardens.component.css',
    '../common/growpod-page-styles.css',
  ],
})

/**
 * This is a component designed to display gardens close to either the current
 * user's zipcode or a provided zipcode.
 *
 * This page takes an argument 'zip-code', the zipcode to search in.
 * If the 'zip-code' argument is not provided, the component will call
 * GET: /find-gardens. Otherwise, the component will call GET:
 * /find-gardens?zip-code={zip-code}, with the value of the string 'zip-code' as {zip-code}.
 */
export class FindGardensComponent implements OnInit {
  gardenList: Array<Garden> | null;
  gardenAdminNames: Map<string, string>;
  isLoaded = false;
  errorMessage = '';
  userGardenSet: Set<string>;
  zipCodeControl: FormControl;
  oAuthToken: string;

  /**
   * Option to display a certian number of slides based off of browser window size along with
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

  /**
   * Initializes the component based on provided arguments
   *
   * @param route Contains arguments.
   */
  constructor(
    private authService: OAuthSession,
    private route: ActivatedRoute,
    private httpClient: HttpClient
  ) {
    // Add simple regex zip code validation.
    this.zipCodeControl = new FormControl('', [
      Validators.pattern('^$|^([0-9]{5})$'),
    ]);
  }

  ngOnInit(): void {
    this.oAuthToken = this.authService.userData.idToken;
    const zipCodeArg = this.route.snapshot.paramMap.get('zip-code');
    this.createNearbyGardenList(zipCodeArg);
    // Changes to zipCodeControl change nearby garden list.
    this.zipCodeControl.valueChanges.subscribe(() => {
      if (this.zipCodeControl.valid) {
        if (this.zipCodeControl.value) {
          this.createNearbyGardenList(this.zipCodeControl.value);
          return;
        }
        // Empty String == Search near me.
        this.createNearbyGardenList(zipCodeArg); // Capture enclosing scope.
      }
    });
  }

  /**
   * Finds gardens close to a given zip code.
   * Returns an observable HTTP response.
   *
   * Performs GET: /find-gardens[?zip-code={zipCode}]
   * If the argument zipCode is null, the query string
   * is omitted.
   *
   * @param zipCode The zipcode to send to the server, or a null value to
   * search near the current user's location.
   * @return the http response.
   */
  getNearbyGardenList(
    zipCode: string | null
  ): Observable<HttpResponse<Array<Garden>>> {
    return this.httpClient.get<Array<Garden>>('/find-gardens', {
      observe: 'response',
      responseType: 'json',
      // Conditionally include parameter on truthiness
      params: {
        token: this.oAuthToken,
        ...(zipCode && {'zip-code': zipCode}),
      },
    });
  }

  /**
   * Gets all gardens this user is a part of.
   *
   * Performs GET: /user/current/garden-list
   *
   * @return the http response.
   */
  getCurrentUserGardenList(): Observable<HttpResponse<Array<String>>> {
    return this.httpClient.get<Array<String>>('/user/current/garden-list', {
      observe: 'response',
      responseType: 'json',
      params: {
        token: this.oAuthToken,
      },
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
      params: {
        token: this.oAuthToken,
      },
    });
  }

  /**
   * Posts the garden id for the current user to join.
   *
   * Performs POST: /user/current/garden-list/{id}, with an empty body.
   *
   * @param id The garden id the user wishes to join
   * @return the http response.
   */
  postJoinGarden(id: string): Observable<HttpResponse<string>> {
    return this.httpClient.post<string>('/user/current/garden-list/' + id, '', {
      observe: 'response',
      responseType: 'json',
      params: {
        token: this.oAuthToken,
      },
    });
  }

  /**
   * Populates component with nearby gardens, shows a message to the user
   * saying there are no gardens, or displays an error message.
   *
   * @param zipCode The zipcode requested or null.
   */
  createNearbyGardenList(zipCode: string | null): void {
    this.createUserGardenSet();
    this.getNearbyGardenList(zipCode).subscribe({
      next: (response: HttpResponse<Array<Garden>>) => {
        // Successful responses are handled here.
        this.gardenList = response.body;
        this.createGardenAdminNames();
        this.isLoaded = true;
      },
      error: (error: HttpErrorResponse) => {
        // Handle connection error
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          this.gardenList = null;
          this.errorMessage = 'Cannot connect to GrowPod Server';
          this.isLoaded = true;
          return;
        }
        console.error('Unexpected error: ' + error.statusText);
        this.gardenList = null;
        this.errorMessage =
          'Unexpected error ' + error.status + ': ' + error.statusText;
        this.isLoaded = true;
      },
    });
  }

  /**
   * Populates userGardenSet, so this page can deactivate the join button
   * for gardens the user is not a part of.
   *
   */
  createUserGardenSet(): void {
    this.getCurrentUserGardenList().subscribe({
      next: (response: HttpResponse<Array<string>>) => {
        // Successful responses are handled here.
        this.userGardenSet = new Set<string>();
        response.body.forEach(elt => this.userGardenSet.add(elt));
        // Don't display anything yet.
      },
      error: (error: HttpErrorResponse) => {
        // Handle connection error
        if (error.error instanceof ErrorEvent) {
          console.error('Network error: ' + error.error.message);
          this.gardenList = null;
          this.errorMessage = 'Cannot connect to GrowPod Server';
          this.isLoaded = true;
          return;
        }
        console.error('Unexpected error: ' + error.statusText);
        this.gardenList = null;
        this.errorMessage =
          'Unexpected error ' + error.status + ': ' + error.statusText;
        this.isLoaded = true;
      },
    });
  }

  /**
   * Populates gardenAdminNames with administer names.
   *
   */
  createGardenAdminNames(): void {
    // Initialize array
    this.gardenAdminNames = new Map<string, string>();
    this.gardenList.forEach(garden => {
      this.gardenAdminNames.set(garden.adminId, 'Loading...');

      // Obtain user names
      this.getUserInfo(garden.adminId).subscribe({
        next: (response: HttpResponse<User>) => {
          // Successful responses are handled here.
          this.gardenAdminNames.set(
            garden.adminId,
            response.body.preferredName
          );
        },
        error: (error: HttpErrorResponse) => {
          // Handle connection error
          if (error.error instanceof ErrorEvent) {
            console.error('Network error: ' + error.error.message);
            this.gardenAdminNames.set(garden.adminId, 'Cannot fetch name');
            return;
          }
          console.error('Unexpected error: ' + error.statusText);
          this.gardenAdminNames.set(garden.adminId, 'Cannot fetch name');
        },
      });
    });
  }

  /**
   * Joins a garden and refreshes the userGardenSet.
   *
   * @param id the id of the garden to join.
   */
  joinGarden(id: string): void {
    this.postJoinGarden(id).subscribe({
      next: () => {
        this.createUserGardenSet();
      },
      error: FindGardensComponent.logError,
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
