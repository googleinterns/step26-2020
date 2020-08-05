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

import {Component, OnInit, Input} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {HttpParams} from '@angular/common/http';
import {FormControl, Validators, FormGroup} from '@angular/forms';

import {Router} from '@angular/router';
import {Garden} from '../model/garden.model';

@Component({
  selector: 'create-gardens',
  templateUrl: './create-gardens.component.html',
  styleUrls: [
    './create-gardens.component.css',
    '../common/growpod-page-styles.css',
  ],
})
export class CreateGardensComponent implements OnInit {
  @Input('email') adminEmail: string;
  gardenName: string;
  gardenDescription: string;
  gardenZip: string;

  gardenProfile: Garden = {
    id: undefined,
    name: undefined,
    description: undefined,
    lat: undefined,
    lng: undefined,
    zipCode: undefined,
    adminId: undefined,
  };

  constructor(private httpClient: HttpClient, private router: Router) {}

  gardenGroup: FormGroup;

  ngOnInit(): void {
    this.gardenGroup = new FormGroup({
      gardenNameValidator: new FormControl(this.gardenName, [
        Validators.required,
      ]),
      gardenDescriptionValidator: new FormControl(this.gardenDescription, [
        Validators.required,
      ]),
      gardenZipValidator: new FormControl(this.gardenZip, [
        Validators.required,
      ]),
    });
  }

  buildGardenProfile(): void {
    this.gardenProfile = {
      id: '2',
      name: this.gardenName,
      description: this.gardenDescription,
      lat: 0.0,
      lng: 0.0,
      zipCode: this.gardenZip,
      adminId: this.adminEmail,
    };
    this.postGardenData(this.gardenProfile);
  }

  postGardenData(data: Garden): void {
    const httpOptions = {
      params: new HttpParams().set('gardenData', JSON.stringify(data)),
    };
    this.httpClient
      .post<Garden>('/garden', null, httpOptions)
      .subscribe(response => {
        //will display a conformation/error message to user based on response (next pr)
        console.log(response);
      });
    this.router.navigate(['page/my-gardens']);
  }
}
