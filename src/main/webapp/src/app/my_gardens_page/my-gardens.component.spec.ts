import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MyGardensComponent} from './my-gardens.component';

describe('MyGardensComponent', () => {
  let component: MyGardensComponent;
  let fixture: ComponentFixture<MyGardensComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MyGardensComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyGardensComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
