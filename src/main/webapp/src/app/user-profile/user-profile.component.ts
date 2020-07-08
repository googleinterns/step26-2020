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

import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../model/user.model';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: [
    './user-profile.component.css',
    '../common/growpod-page-styles.css',
  ],
})

/**
 * This is a component designed to display a user profile, either the
 * logged in user or another user.
 *
 * This page takes an argument 'id', the id of the user it requests.
 * If the 'id' argument is not provided, the component will call
 * GET: /user/current. Otherwise, the component will call GET:
 * /user/{id}, with the value of the string 'id' as {id}.
 */
export class UserProfileComponent implements OnInit {
  displayInfo: User | null;
  errorMessage = '';

  /**
   * Initializes the component based on provided arguments
   *
   * @param route Contains arguments.
   */
  constructor(private route: ActivatedRoute, private httpClient: HttpClient) {
    const idArg = route.snapshot.paramMap.get('id');
    const id = idArg ?? 'current';
    this.createUserProfile(id);
  }

  ngOnInit(): void {}

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
   * Populates component to create a profile, or shows an
   * error message if profile does not exist.
   *
   * @param user The user requested.
   */
  createUserProfile(user: string): void {
    this.getUserInfo(user).subscribe({
      next: response => {
        // Successful responses are handled here.
        this.displayInfo = response.body;
      },
      error: error => {
        // Error messages are handled here.
        this.displayInfo = null;
        this.errorMessage = 'Cannot see user profile for user id: ' + user;
        console.log(new Error(error));
      },
    });
  }
}
