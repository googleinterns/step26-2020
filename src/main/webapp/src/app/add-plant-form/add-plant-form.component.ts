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

import {Component, OnInit} from '@angular/core';
import {FormGroup, FormControl, Validators} from '@angular/forms';
import {MatDialogRef} from '@angular/material/dialog';
import {Plant} from '../model/plant.model';

@Component({
  selector: 'add-plant-form',
  templateUrl: './add-plant-form.component.html',
  styleUrls: [
    './add-plant-form.component.css',
    '../common/growpod-page-styles.css',
  ],
})

/**
 * Simple modal for adding a plant.
 *
 */
export class AddPlantComponent implements OnInit {
  plant: Plant = {
    id: "1",
    nickname: undefined,
    count: undefined,
    plantTypeId: "1",
  };
  plantGroup: FormGroup;

  constructor(public dialogRef: MatDialogRef<AddPlantComponent, Plant>) {}

  ngOnInit(): void {
    this.plantGroup = new FormGroup({
      nickname: new FormControl(this.plant.nickname, [
        Validators.required,
        Validators.minLength(1),
      ]),
      count: new FormControl(this.plant.count, [
        Validators.required,
        Validators.pattern("[0-9]+"),
        Validators.min(1),
      ])
    });
  }

  close(): void {
    this.dialogRef.close();
  }

  /**
   * Closes the dialog with plant provided
   * the input is valid.
   *
   */
  submit(): void {
    if (this.plantGroup.valid) {
      this.plant = this.plantGroup.value;
      this.plant.id = "1";
      this.plant.plantTypeId = "1";
      this.dialogRef.close(this.plant);
    }
  }
}
