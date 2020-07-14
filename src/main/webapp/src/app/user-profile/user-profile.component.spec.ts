import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {RouterModule, ActivatedRoute, convertToParamMap} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {UserProfileComponent} from './user-profile.component';
import {User} from '../model/user.model';

describe('UserProfileComponent', () => {
  let component: UserProfileComponent;
  let fixture: ComponentFixture<UserProfileComponent>;
  let httpTestingController: HttpTestingController;

  // Component-local mocks
  const MOCK_ARGUMENT = '0';
  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({
        id: MOCK_ARGUMENT,
      }),
    },
  };
  const MOCK_OK_RESPONSE: User = {
    id: MOCK_ARGUMENT,
    email: 'example@example.com',
    preferredName: 'User name',
    biography: 'User biography',
    zipCode: '12345',
  };

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterModule, GrowpodUiModule],
      declarations: [UserProfileComponent],
      providers: [{provide: ActivatedRoute, useValue: mockActivatedRoute}],
    }).compileComponents();
  }));

  beforeEach(() => {
    // Inject dependencies
    TestBed.inject(ActivatedRoute);
    TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    // Create component
    fixture = TestBed.createComponent(UserProfileComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  /**
   * Test: Should decode the parameter `id` and make
   * the appropriate GET request /user/{id}.
   */
  it('should make a GET request for a given user ID', () => {
    fixture.detectChanges();

    const testRequest = httpTestingController.expectOne(
      '/user/' + MOCK_ARGUMENT,
      "A properly-formed GET request to get a user's profile"
    );
    expect(testRequest.request.method).toBe('GET');
  });

  /**
   * Test: Should populate displayInfo with the Mock OK response,
   * displaying it on the page.
   */
  it('should display a user profile page', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + MOCK_ARGUMENT
    );
    testRequest.flush(MOCK_OK_RESPONSE);
    fixture.detectChanges();

    expect(component.displayInfo).toBeTruthy();
    expect(component.displayInfo).toBe(MOCK_OK_RESPONSE);
  });

  /**
   * Test: Should display an error message as a result of a mock error
   * code.
   */
  it('should display an error message due to an error code', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + MOCK_ARGUMENT
    );
    testRequest.flush(
      {},
      {
        status: 404,
        statusText: 'Not found',
      }
    );
    fixture.detectChanges();

    expect(component.displayInfo).not.toBeTruthy();
    expect(component.errorMessage).toBeTruthy();
  });
});
