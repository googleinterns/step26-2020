import {Component} from '@angular/core';
import {SocialAuthService} from 'angularx-social-login';
import {GoogleLoginProvider} from 'angularx-social-login';
import {HttpClient} from '@angular/common/http';
import {HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Component({
  selector: 'login',
  templateUrl: './Login-SignUp.component.html',
  styleUrls: ['./Login-SignUp.component.css'],
})
export class LoginComponent {
  user: any;
  userData: any;

  constructor(
    private authService: SocialAuthService,
    private httpClient: HttpClient
  ) {}

  signInWithGoogle() {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(response => {
      this.user = response;
      // this.userData= {id:this.user.email,email:this.user.email,name:this.user.name,bio:"hi",zip:"14214"};
    });

    return (this.userData = {
      id: 'email',
      email: 'email',
      name: 'kayla',
      bio: 'hi',
      zip: '14214',
    });
  }

  getUserData() {
    return this.userData;
  }

  signOut(): void {
    this.authService.signOut();
    console.log('user signed out');
  }

  createData(createBody: any): Observable<any> {
    console.log('this is user data', this.signInWithGoogle());
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
      }),
      params: new HttpParams().set('name', JSON.stringify(createBody)),
    };
    return this.httpClient.post<any>('/user', null, httpOptions);
  }

  addNewUser() {
    const newUser = {name: 'kayla'};
    this.createData(newUser).subscribe(res => {
      console.log(res, 'thi is response');
      console.log(newUser);
    });
  }
}
