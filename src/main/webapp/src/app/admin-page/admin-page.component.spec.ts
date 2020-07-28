import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {AdminPageComponent} from './admin-page.component';
import {DatepickerComponent} from '../datepicker/datepicker.component';

describe('AdminPageComponent', () => {
  let component: AdminPageComponent;
  let fixture: ComponentFixture<AdminPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GrowpodUiModule],
      declarations: [AdminPageComponent, DatepickerComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
