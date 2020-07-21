import {Injectable} from '@angular/core';
import {UserRepository} from '../repositories/user.repository';

const CLIENT_ID = '<generate client id';
const API_KEY = '<generate api key>';

const DISCOVERY_DOCS = [
  'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest',
];
const SCOPES = 'https://www.googleapis.com/auth/calendar';

@Injectable()
export class GapiSession {
  googleAuth: gapi.auth2.GoogleAuth;
  userRepository: UserRepository;

  constructor() {
    this.userRepository = new UserRepository();
  }

  /**
   * Include docs
   */
  initClient() {
    return new Promise((resolve) => {
      gapi.load('client:auth2', () => {
        return gapi.client
          .init({
            apiKey: API_KEY,
            clientId: CLIENT_ID,
            discoveryDocs: DISCOVERY_DOCS,
            scope: SCOPES,
          })
          .then(() => {
            this.googleAuth = gapi.auth2.getAuthInstance();
            resolve();
          });
      });
    });
  }

  /**
   * Include docs
   */
  get isSignedIn(): boolean {
    return this.googleAuth.isSignedIn.get();
  }

  /**
   * Include docs
   */
  signIn() {
    return this.googleAuth
      .signIn({
        prompt: 'consent',
      })
      .then((googleUser: gapi.auth2.GoogleUser) => {
        this.userRepository.add(googleUser.getBasicProfile());
      });
  }

  signOut(): void {
    this.googleAuth.signOut();
  }
}
