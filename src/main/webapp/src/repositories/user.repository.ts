import {Injectable} from '@angular/core';
import {User} from './model/user';

const USERS = 'users';

@Injectable()
export class UserRepository {
  /* Adding signed in user to storage */
  add(profile: gapi.auth2.BasicProfile) {
    const users = this.getAll();

    // Determine whether signed user id is already on local storage
    let foundIndex = -1;
    for (let i = 0; i < users.length; i++) {
      if (users[i].id === profile.getId()) {
        foundIndex = i;
        break;
      }
    }
    // Remove user at found index
    if (foundIndex >= 0) {
      users.splice(foundIndex, 1);
    }

    // Add and save signed user to storage
    users.push(User.fromBasicProfile(profile));
    this.save(users);
  }

  /* Get all stored users */
  getAll(): User[] {
    const data = localStorage.getItem(USERS);
    if (data) {
      return <User[]>JSON.parse(data);
    } else {
      return [];
    }
  }

  /* Save users into a local storage in the cliend side */
  save(users: User[]) {
    localStorage.setItem(USERS, JSON.stringify(users));
  }
}
