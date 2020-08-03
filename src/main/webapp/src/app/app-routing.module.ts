// Copyright 2020 Google LLCn
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
import {Routes, RouterModule} from '@angular/router';

import {MyGardensComponent} from './my_gardens_page/my-gardens.component';
import {UserProfileComponent} from './user-profile/user-profile.component';
import {FindGardensComponent} from './find-gardens-page/find-gardens.component';
import {SchedulePageComponent} from './schedule-page/schedule-page.component';
import {LoginComponent} from './login-signup-page/login-signup.component';
import {CreateGardensComponent} from './create-gardens-form/create-gardens.component';
import {AdminPageComponent} from './admin-page/admin-page.component';

/**
 * The routing table for all frontend pages
 *
 * Contains a list of objects that associate
 * URL paths with Angular components.
 *
 */
const routes: Routes = [
  {path: 'page/my-gardens', component: MyGardensComponent},
  {path: 'page/user-profile', component: UserProfileComponent},
  {path: 'page/find-gardens', component: FindGardensComponent},
  {path: 'page/schedule', component: SchedulePageComponent},
  {path: 'page/login', component: LoginComponent},
  {path: 'page/create-garden', component: CreateGardensComponent},
  {path: 'page/admin-page', component: AdminPageComponent},
  /** Defaults to /my-gardens */
  {path: '', redirectTo: '/page/my-gardens', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
