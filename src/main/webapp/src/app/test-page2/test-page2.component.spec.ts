import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestPage2Component } from './test-page2.component';

describe('TestPage2Component', () => {
  let component: TestPage2Component;
  let fixture: ComponentFixture<TestPage2Component>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestPage2Component ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestPage2Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
