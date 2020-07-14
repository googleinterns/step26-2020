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

import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {MaterialComponents} from './common/material-components';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {CarouselModule} from 'ngx-owl-carousel-o';
import {RouterModule} from '@angular/router';
import {MatExpansionModule} from '@angular/material/expansion';
import {MatCardModule} from '@angular/material/card';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatNativeDateModule} from '@angular/material/core';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';

import {AppComponent} from './app.component';
import {IndexComponent} from './index/index.component';
import {TestPage1Component} from './test-page1/test-page1.component';
import {TestPage2Component} from './test-page2/test-page2.component';
import {CreateGardensComponent} from './create-gardens-form/create-gardens.component';
import {MyGardensComponent} from './my_gardens_page/my-gardens.component';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {FindGardensComponent} from './find-gardens-page/find-gardens.component';
import {SchedulePageComponent} from './schedule-page/schedule-page.component';
import {TaskComponent} from './task/task.component';

@NgModule({
  declarations: [
    AppComponent,
    IndexComponent,
    TestPage1Component,
    TestPage2Component,
    CreateGardensComponent,
    MyGardensComponent,
    UserProfileComponent,
    FindGardensComponent,
    SchedulePageComponent,
    TaskComponent,
  ],
  imports: [
    MaterialComponents,
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatFormFieldModule,
    MatNativeDateModule,
    MatSelectModule,
    CarouselModule,
    RouterModule,
    MatExpansionModule,
    FormsModule,
    ReactiveFormsModule,
    MatDatepickerModule,
    MatInputModule,
    MatNativeDateModule,
  ],
  providers: [
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: {appearance: 'fill'}},
  ],

  bootstrap: [AppComponent],
})
export class AppModule {}
