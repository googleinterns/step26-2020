import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {By} from '@angular/platform-browser';
import {ActivatedRoute} from '@angular/router';
import {RouterTestingModule} from '@angular/router/testing';
import {HttpClient} from '@angular/common/http';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import {GrowpodUiModule} from '../common/growpod-ui.module';
import {FindGardensComponent} from './find-gardens.component';
import {Garden} from '../model/garden.model';

/* TODO: Find a way to test carousel components with arguments. */
describe('FindGardensComponent', () => {
  let component: FindGardensComponent;
  let fixture: ComponentFixture<FindGardensComponent>;
  let httpTestingController: HttpTestingController;

  // Component-local test values and mocks
  const TEST_ARGUMENT = '12345';
  const TEST_GARDEN: Garden = {
    id: '0',
    name: 'Garden name',
    description: 'Garden description',
    lat: 0,
    lng: 0,
    zipCode: TEST_ARGUMENT,
    adminId: '0',
  };
  const TEST_GARDEN_LIST: Array<Garden> = [TEST_GARDEN];
  const TEST_USER_GARDEN_LIST: Array<string> = ['0'];
  const TEST_USER_GARDEN_LIST_ALT: Array<string> = ['1'];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule, GrowpodUiModule],
      declarations: [FindGardensComponent],
      providers: [],
    }).compileComponents();
  }));

  beforeEach(() => {
    // Inject dependencies
    TestBed.inject(ActivatedRoute);
    TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    // Create component
    fixture = TestBed.createComponent(FindGardensComponent);
    component = fixture.componentInstance;
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should decode the parameter zip-code and make the GET requests /user/current/garden-list and /find-gardens', () => {
    fixture.detectChanges();

    // User garden list is requested first.
    const testUserRequest = httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    expect(testUserRequest.request.method).toBe('GET');

    // Then find-gardens is requested.
    const testGardenRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A properly-formed GET request to find nearby gardens'
    );
    expect(testGardenRequest.request.method).toBe('GET');
  });

  it('should populate displayInfo as a result of a mock OK response object', () => {
    fixture.detectChanges();

    // Handle requests
    httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    const testRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testRequest.flush(TEST_GARDEN_LIST);

    // Ensure all requests are flushed. Not used in this test.
    httpTestingController.expectOne(
      '/user/0',
      'A GET request for user profile information'
    );
    fixture.detectChanges();

    expect(component.displayInfo).toBe(TEST_GARDEN_LIST);
  });

  it('should display a message indicating no gardens were found nearby', () => {
    fixture.detectChanges();

    // Handle requests
    httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    const testRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testRequest.flush([]);
    fixture.detectChanges();

    expect(component.displayInfo).toEqual([]);
    // Verifies error message. Second child of h4 tag.
    const message = fixture.debugElement.query(By.css('h4'));
    expect(message.nativeElement.childNodes[1].nodeValue.trim()).toBe(
      'No nearby gardens found.'
    );
  });

  it('should display an error message as a result of an unexpected error code', () => {
    fixture.detectChanges();

    // Handle requests
    httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    const testRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testRequest.flush(
      {},
      {
        status: 500,
        statusText: 'Internal Server Error',
      }
    );
    fixture.detectChanges();

    expect(component.displayInfo).not.toBeTruthy();
    expect(component.errorMessage).toBe(
      'Unexpected error 500: Internal Server Error'
    );
  });

  it('should display an error message indicating network failure', () => {
    fixture.detectChanges();

    // Handle requests
    httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    const testRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testRequest.error(
      new ErrorEvent('error', {
        error: new Error('Network Failure'),
        message: 'Cannot connect to server',
      })
    );
    fixture.detectChanges();

    expect(component.displayInfo).toBeNull();
    expect(component.errorMessage).toBe('Cannot connect to GrowPod Server');
  });

  it('should display that the user has already joined the returned garden', () => {
    fixture.detectChanges();

    // Handle requests
    const testUserRequest = httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    testUserRequest.flush(TEST_USER_GARDEN_LIST);
    const testGardenRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testGardenRequest.flush(TEST_GARDEN_LIST);

    // Result not used in this test
    httpTestingController.expectOne(
      '/user/0',
      'A GET request for user profile information'
    );

    // Check that the garden is marked as joined
    fixture.detectChanges();
    // Obtain all carousel slides.
    const carousel = fixture.debugElement.queryAll(By.css('mat-card'));
    expect(carousel.length).toBe(1);
    // Expect first carousel slide to have disabled join button.
    const disabledButton = carousel[0].query(By.css('button[disabled]'));
    expect(disabledButton.nativeElement.textContent.trim()).toBe(
      'Already joined'
    );
  });

  it('should display a join garden button for the returned garden', () => {
    fixture.detectChanges();

    // Handle requests
    const testUserRequest = httpTestingController.expectOne(
      '/user/current/garden-list',
      "A properly-formed GET request to get a user's garden list"
    );
    // This list does not contain the garden in TEST_GARDEN_LIST
    testUserRequest.flush(TEST_USER_GARDEN_LIST_ALT);
    const testGardenRequest = httpTestingController.expectOne(
      '/find-gardens',
      'A GET request to return gardens'
    );
    testGardenRequest.flush(TEST_GARDEN_LIST);

    // Result not used in this test
    httpTestingController.expectOne(
      '/user/0',
      'A GET request for user profile information'
    );

    // Check that the garden is marked as joined
    fixture.detectChanges();
    // Obtain all carousel slides.
    const carousel = fixture.debugElement.queryAll(By.css('mat-card'));
    expect(carousel.length).toBe(1);
    // Expect first carousel slide to have no disabled join button.
    const joinButton = carousel[0].query(By.css('button'));
    expect(joinButton.attributes).not.toContain('disabled');
    expect(joinButton.nativeElement.textContent.trim()).toBe('Join Garden');
  });
});
