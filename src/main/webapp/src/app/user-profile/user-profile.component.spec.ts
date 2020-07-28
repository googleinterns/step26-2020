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
  const TEST_ARGUMENT = '0';
  const mockActivatedRoute = {
    snapshot: {
      paramMap: convertToParamMap({
        id: TEST_ARGUMENT,
      }),
    },
  };
  const TEST_OK_RESPONSE: User = {
    id: TEST_ARGUMENT,
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

  it('should decode the parameter id and make the appropriate GET request /user/{id}', () => {
    fixture.detectChanges();

    const testRequest = httpTestingController.expectOne(
      '/user/' + TEST_ARGUMENT,
      "A properly-formed GET request to get a user's profile"
    );
    expect(testRequest.request.method).toBe('GET');
  });

  it('should populate userProfile as a result of a mock OK response object', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + TEST_ARGUMENT
    );
    testRequest.flush(TEST_OK_RESPONSE);
    fixture.detectChanges();

    expect(component.userProfile).toBe(TEST_OK_RESPONSE);
  });

  it('should display an error message as a result of a mock error code 404', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + TEST_ARGUMENT
    );
    testRequest.flush(
      {},
      {
        status: 404,
        statusText: 'Not found',
      }
    );
    fixture.detectChanges();

    expect(component.userProfile).toBeNull();
    expect(component.errorMessage).toBe(
      'Cannot see user profile for user id: ' + TEST_ARGUMENT
    );
  });

  it('should display a different error message as a result of any other error code', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + TEST_ARGUMENT
    );
    testRequest.flush(
      {},
      {
        status: 405,
        statusText: 'Method not allowed',
      }
    );
    fixture.detectChanges();

    expect(component.userProfile).toBeNull();
    expect(component.errorMessage).toBe(
      'Unexpected error 405: Method not allowed'
    );
  });

  it('should display an error message indicating network failure', () => {
    fixture.detectChanges();

    // Handle request
    const testRequest = httpTestingController.expectOne(
      '/user/' + TEST_ARGUMENT
    );
    testRequest.error(
      new ErrorEvent('error', {
        error: new Error('Network Failure'),
        message: 'Cannot connect to server',
      })
    );
    fixture.detectChanges();

    expect(component.userProfile).toBeNull();
    expect(component.errorMessage).toBe('Cannot connect to GrowPod Server');
  });
});
