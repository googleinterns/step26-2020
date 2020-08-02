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
//import {SocialAuthService} from 'angularx-social-login';
//import {GoogleLoginProvider} from 'angularx-social-login';
//import {HttpClient} from '@angular/common/http';
//import {HttpHeaders, HttpParams} from '@angular/common/http';
//import {Router} from '@angular/router';
import {FormControl, Validators,FormGroup} from '@angular/forms';
//import {User} from '../model/user.model';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'new-user-form',
  templateUrl: './new-user-form.component.html',
  styleUrls: [
    './new-user-form.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class NewUserFormComponent {
  //user: any;
  bio: string;
  zipCode: string;
  //newUser=false;
  choice:string;
/*
  userProfile: User = {
    id: undefined,
    email: undefined,
    preferredName: undefined,
    biography: undefined,
    zipCode: undefined,
  };
*/
  constructor(
   // private authService: SocialAuthService,
    //private httpClient: HttpClient,
    //private router: Router,
    public dialog: MatDialog,
  ) {}

  userGroup:FormGroup;

  

ngOnInit(): void {
    this.userGroup = new FormGroup({
      zipCodeValidator: new FormControl(this.zipCode, [
        Validators.required,
        //Validators.minLength(1),
      ]),
      bioValidator: new FormControl(this.bio, [
        Validators.required,
        //Validators.pattern('[0-9]+'),
        //Validators.min(1),
      ]),
       gardenValidator: new FormControl(this.choice, [
        Validators.required,
        //Validators.pattern('[0-9]+'),
        //Validators.min(1),
      ]),
    });
  }
  
 

 
}
