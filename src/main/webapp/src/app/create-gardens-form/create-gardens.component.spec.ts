import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CreateGardensComponent} from './create-gardens.component';

describe('CreateGardensComponent', () => {
  let component: CreateGardensComponent;
  let fixture: ComponentFixture<CreateGardensComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CreateGardensComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateGardensComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
