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

import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {IndexComponent} from './index/index.component';
import {TestPage1Component} from './test-page1/test-page1.component';
import {TestPage2Component} from './test-page2/test-page2.component';
import {MyGardensComponent} from './my_gardens_page/my-gardens.component';
import {FindGardensComponent} from './find-gardens-page/find-gardens.component';
import {SchedulePageComponent} from './schedule-page/schedule-page.component';

/**
 * The routing table for all frontend pages
 *
 * Contains a list of objects that associate
 * URL paths with Angular components.
 *
 */
const routes: Routes = [
  {path: 'page/index', component: IndexComponent},
  {path: 'page/test-page1', component: TestPage1Component},
  {path: 'page/test-page2', component: TestPage2Component},
  {path: 'page/my-gardens', component: MyGardensComponent},
  {path: 'page/find-gardens', component: FindGardensComponent},
  {path: 'page/schedule', component: SchedulePageComponent},
  /** Defaults to /index */
  {path: '', redirectTo: '/page/index', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
