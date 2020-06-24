import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestPage1Component } from './test-page1.component';

describe('TestPage1Component', () => {
  let component: TestPage1Component;
  let fixture: ComponentFixture<TestPage1Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestPage1Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestPage1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
