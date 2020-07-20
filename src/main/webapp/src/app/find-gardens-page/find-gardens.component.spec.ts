import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {FindGardensComponent} from './find-gardens.component';

describe('FindGardens', () => {
  let component: FindGardensComponent;
  let fixture: ComponentFixture<FindGardensComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GrowpodUiModule],
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
