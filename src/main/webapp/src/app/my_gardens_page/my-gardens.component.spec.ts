import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {MyGardensComponent} from './my-gardens.component';

describe('MyGardensComponent', () => {
  let component: MyGardensComponent;
  let fixture: ComponentFixture<MyGardensComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GrowpodUiModule],
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
