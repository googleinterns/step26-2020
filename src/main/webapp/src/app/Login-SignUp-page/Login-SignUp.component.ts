import {Component} from '@angular/core';
import {SocialAuthService } from "angularx-social-login";
import {GoogleLoginProvider } from "angularx-social-login";
import{HttpClient} from '@angular/common/http';

@Component({
  selector: 'login',
  templateUrl: './Login-SignUp.component.html',
  styleUrls: ['./Login-SignUp.component.css']
})
export class LoginComponent  {
 user: any;
 //name:string;
 //email:string;
 //imgsrc:string;
 name="kayla";


 postData1={
     test:"kayla",
 };

constructor(private authService: SocialAuthService,private http:HttpClient) { }
 
  signInWithGoogle():void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then((response)=>{
      console.log("user is logged in user data is =",response);  
     this.user=response;
     this.postData(this.user);
  
  });
  }

 
  signOut(): void {
    this.authService.signOut();
    console.log("user signed out");
  }

 postData(user){
     this.user=user;
     console.log("user,",this.user);

     this.http.post("/user",this.name).toPromise().then((data:string)=>
     {
         console.log(data)
         console.log("name,",this.name)

     });
    

    /*this.http.post<any>('user', { title: 'Angular POST Request Example' }).subscribe(data => {
    this.postId = data.id;
})
*/
     console.log("got here java",this.user,this.name);
    
 }
 
}