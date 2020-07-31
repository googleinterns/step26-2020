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
import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {FormControl, Validators} from '@angular/forms';

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
  bio:String;
  zipCode:String;
  userProfile:any;

  constructor(
    private authService: SocialAuthService,
    private httpClient: HttpClient,
    private router: Router
  ) {}

  //email = new FormControl('', [Validators.required]);

  //getErrorMessage() {
   // if (this.email.hasError('required')) {
     // return 'You must enter a value';
   // }

    //return this.email.hasError('email') ? 'Not a valid email' : '';
  //}

  buildUserProfile():void{
      console.log("user profle****");
      this.userProfile={id:this.user.email,email:this.user.email,preferredName:this.user.name,bio:this.bio,zipCode:this.zipCode};
      console.log("this is up"+ this.userProfile.bio);
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
      }
      else{
          console.log("got here sign up*****");
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
  postData(data: any): void {
      console.log("got here postdata*****")
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      params: new HttpParams().set('userData', JSON.stringify(data)),
    };
    console.log(JSON.stringify(data)+ " stringifyed data");
    this.httpClient.post<any>('/user', null, httpOptions);
  }
}
