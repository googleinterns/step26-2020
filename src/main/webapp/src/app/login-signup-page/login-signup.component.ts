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

import {Component} from '@angular/core';
import {SocialAuthService} from 'angularx-social-login';
import {GoogleLoginProvider} from 'angularx-social-login';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {FormControl, Validators, FormGroup} from '@angular/forms';
import {Observable} from 'rxjs';

import {Router} from '@angular/router';
import {User} from '../model/user.model';

@Component({
  selector: 'login',
  templateUrl: './login-signup.component.html',
  styleUrls: [
    './login-signup.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class LoginComponent {
  user: any;
  bio: string;
  done = false;
  zipCode: string;
  newUser = false;
  choice: string; //joining or creating a garden
  admin = false; // if the user is done signing up

  userProfile: User = {
    id: undefined,
    email: undefined,
    preferredName: undefined,
    biography: undefined,
    zipCode: undefined,
  };

  constructor(
    private authService: SocialAuthService,
    private httpClient: HttpClient,
    private router: Router
  ) {}

  userGroup: FormGroup;

  ngOnInit(): void {
    this.userGroup = new FormGroup({
      zipCodeValidator: new FormControl(this.zipCode, [Validators.required]),
      bioValidator: new FormControl(this.bio, [Validators.required]),
      gardenValidator: new FormControl(this.choice, [Validators.required]),
    });
  }

  buildUserProfile(): void {
    this.userProfile = {
      id: '1',
      email: this.user.email,
      preferredName: this.user.name,
      biography: this.bio,
      zipCode: this.zipCode,
    };

    if (this.choice === 'create') {
      this.admin = true;
      console.log(this.admin);
    } else if (this.choice === 'join') {
      this.done = true;
      console.log(this.done);
      //this.router.navigate(['page/my-gardens']);
    }
    this.postData(this.userProfile);
  }

  /**
   * @param accountData user data taken from google account: email and name
   */
  set userData(accountData: any) {
    this.user = accountData;
  }

  /**
   * @return user data taken from google account: email and name
   */
  get userData(): any {
    return this.user;
  }

  /**
   * Sign in function that allows the user to sign in with their google email using Google OAuth
   * If the user is logging in(assumed that the user is not new)
   * then the user redirected to the my gardens page
   *
   * I will add a flow for a user signing up (assumed new user)
   *
   * @param action string that represents if the user is logging in or signing up
   */
  signInWithGoogle(action: String): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(response => {
      this.user = response;
      if (action === 'login') {
        this.router.navigate(['page/my-gardens']);
      } else {
        this.newUser = true;
      }
    });
  }

  /**
   * This sign out function will be utilized in the user profile page later on
   */
  signOut(): void {
    this.authService.signOut();
  }

  /**
   * This function is responsible for sending the POST request to the servlet.
   * It takes in a JSON object containing user data: id,email,name,bio,zip
   *
   * @param data object holding user data that will be used as a param in the post request
   */
  postData(data: User): void {
    console.log(this.admin, this.done);

    const httpOptions = {
      params: new HttpParams().set('userData', JSON.stringify(data)),
    };
    this.httpClient
      .post<User>('/user', null, httpOptions)
      .subscribe(response => {
        //will display a conformation/error message to user based on response (next pr)
      });
    //console.log(this.user.getKey(this.user.email));
  }
  redirect(): void {
    this.router.navigate(['page/my-gardens']);
  }
  /**
  postPlantToGarden(plant: Plant): Observable<HttpResponse<string>> {
    return this.httpClient.post<string>(
      '/garden/' + this.gardenProfile.id + '/plant-list',
      plant,
      {
        observe: 'response',
        responseType: 'json',
      }
    ); */
}
