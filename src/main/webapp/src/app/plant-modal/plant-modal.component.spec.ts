import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {PlantModalComponent} from './plant-modal.component';

describe('PlantModalComponent', () => {
  let component: PlantModalComponent;
  let fixture: ComponentFixture<PlantModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [GrowpodUiModule],
      declarations: [PlantModalComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PlantModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
