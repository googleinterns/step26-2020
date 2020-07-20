import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {SchedulePageComponent} from './schedule-page.component';
import {DatepickerComponent} from '../datepicker/datepicker.component';

describe('SchedulePageComponent', () => {
  let component: SchedulePageComponent;
  let fixture: ComponentFixture<SchedulePageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GrowpodUiModule],
      declarations: [SchedulePageComponent, DatepickerComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SchedulePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
