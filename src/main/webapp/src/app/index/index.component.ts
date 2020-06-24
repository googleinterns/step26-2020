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

import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-index',
  templateUrl: './index.component.html',
  /** Here, I import page-specific styles as well as a common stylesheet. */
  styleUrls: ['./index.component.css', '../common/growpod-page-styles.css'],
})
export class IndexComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {}

  /**
   * Directs the user to page 'page/test-page2', with the argument
   * 'argument' set to a user-provided value.
   *
   * @param arg the argument to send to 'page/test-page2'
   */
  navPage2(arg: String): void {
    this.router.navigate(['/page/test-page2', {argument: arg}]);
  }
}
