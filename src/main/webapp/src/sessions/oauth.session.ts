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

import {Injectable} from '@angular/core';
import {SocialAuthService, SocialUser} from 'angularx-social-login';
import {GoogleLoginProvider} from 'angularx-social-login';
import {CLIENT_ID} from '../app/SensitiveData';

/**
 * Wrapper service for SocialAuthService so it can be used across multiple pages.
 */
@Injectable()
export class OAuthSession {
  user: SocialUser;
  signedIn = false;

  constructor(private authService: SocialAuthService) {}

  ngOnInit() {}

  /**
   * Returns whether user is signed in with Google
   */
  get isSignedIn(): boolean {
    return this.signedIn;
  }

  /**
   * Returns user data.
   */
  get userData(): SocialUser {
    return this.user;
  }

  /**
   * Google Authentication; displays consent prompt for Google Calendar API
   */
  async signIn(): Promise<SocialUser> {
    const response = await this.authService.signIn(
      GoogleLoginProvider.PROVIDER_ID
    );
    this.user = response;
    return response;
  }

  /**
   * Sign out from Google Auth
   */
  signOut(): void {
    this.authService.signOut();
  }
}
