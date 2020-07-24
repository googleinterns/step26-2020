import { NgZone, Injectable, Optional} from '@angular/core';
import {UserRepository} from '../repositories/user.repository';
import { environment } from '../environments/environment';

const CLIENT_ID = '397696466543-0biqdptbuhjmkjmakg2mo2dsov74dl0s.apps.googleusercontent.com';
const API_KEY = environment.calendar.API_KEY;

const DISCOVERY_DOCS = [
  'https://www.googleapis.com/discovery/v1/apis/drive/v3/rest',
];
const SCOPES = 'https://www.googleapis.com/auth/calendar';

declare const gapi:any;

@Injectable()
export class GapiSession {
  public auth2: any;
  userRepository: UserRepository;

  constructor(private zone: NgZone) {
    this.userRepository = new UserRepository();
  }

  ngOnInit() {
    this.loadClient();
  }

  // add docs
  loadClient(): void {
    gapi.load('client:auth2', () => {
      gapi.auth2.init({
        apiKey: API_KEY,
        clientId: CLIENT_ID,
        discoveryDocs: DISCOVERY_DOCS,
        scope: SCOPES, 
      }).then((auth) => {
        this.zone.run(() => {
          this.auth2 = auth;  
        });
      });
    });
  }

  /**
   * Include docs
   */
  get isSignedIn(): boolean {
    return this.auth2.isSignedIn.get();
  }

  /**
   * Include docs
   */
  signIn() {
    return this.auth2
      .signIn({
        prompt: 'consent',
      })
      .then((googleUser: gapi.auth2.GoogleUser) => {
        this.userRepository.add(googleUser.getBasicProfile());
      });
  }

  signOut(): void {
    this.auth2.signOut();
  }

  listEvents() {
    gapi.client.load('calendar', 'v3', () => {
      gapi.client.calendar.events.list({
              'calendarId' : 'primary',
              'timeMin': (new Date()).toISOString(),
              'singleEvents': true, 
              'maxResults': 10, 
              'orderBy': 'startTime'
      }).then((response) => {
        const events = response.result.items;

        if(events.length > 0) {
          for(let i = 0; i < events.length; i++) {
            let event = events[i];
            console.log(event.summary);
            console.log(event.description);
            console.log(event.start.date);
            console.log(event.start.dateTime);
            console.log(event.attendees);
            console.log("--------------");
          }
        } 
      });
    });
  }
  
}
