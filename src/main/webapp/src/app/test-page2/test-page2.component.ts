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
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-test-page2',
  templateUrl: './test-page2.component.html',
  styleUrls: [
    './test-page2.component.css',
    '../common/growpod-page-styles.css',
  ],
})

/**
 * This is a component designed to take an argument 'argument',
 * and display it on the page (by storing it in the 'argument' field)
 *
 */
export class TestPage2Component implements OnInit {
  argument = '';

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    // extract argument parameter
    const p = this.route.snapshot.paramMap.get('argument');
    // place into component, and thus into DOM via template variable.
    this.argument = p === null ? 'No parameter passed' : p;
  }
}
