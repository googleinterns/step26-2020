import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {FindGardensComponent} from './find-gardens.component';

describe('FindGardens', () => {
  let component: FindGardensComponent;
  let fixture: ComponentFixture<FindGardensComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FindGardensComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FindGardensComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
