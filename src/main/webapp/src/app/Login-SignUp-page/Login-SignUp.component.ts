import {Component, OnInit} from '@angular/core';
import {SocialAuthService } from "angularx-social-login";
import {GoogleLoginProvider } from "angularx-social-login";
 
@Component({
  selector: 'login',
  templateUrl: './Login-SignUp.component.html',
  styleUrls: ['./Login-SignUp.component.css']
})
export class LoginComponent implements OnInit {
 
 constructor(private authService: SocialAuthService) { }
 
  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }
 
  signOut(): void {
    this.authService.signOut();
  }
  
  ngOnInit() {
    }
 
}