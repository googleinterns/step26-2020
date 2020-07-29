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

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {RouterModule} from '@angular/router';

import {AppRoutingModule} from './app-routing.module';
import {GrowpodUiModule} from './common/growpod-ui.module';

import {AppComponent} from './app.component';

import {MyGardensComponent} from './my_gardens_page/my-gardens.component';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {FindGardensComponent} from './find-gardens-page/find-gardens.component';
import {SchedulePageComponent} from './schedule-page/schedule-page.component';
import {CreateGardensComponent} from './create-gardens-form/create-gardens.component';
import {DatepickerComponent} from './datepicker/datepicker.component';
import {AdminPageComponent} from './admin-page/admin-page.component';
import {AddPlantComponent} from './add-plant-form/add-plant-form.component';

@NgModule({
  declarations: [
    AppComponent,
    MyGardensComponent,
    UserProfileComponent,
    FindGardensComponent,
    SchedulePageComponent,
    CreateGardensComponent,
    DatepickerComponent,
    AdminPageComponent,
    AddPlantComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    RouterModule,
    GrowpodUiModule,
  ],
  entryComponents: [
    AddPlantComponent,
  ],

  bootstrap: [AppComponent],
})
export class AppModule {}
