
/* User class for gapi authentication */
export class User {
  id: string;
  email: string;

  static fromBasicProfile(profile: gapi.auth2.BasicProfile): User {
    const user: User = new User();
    user.id = profile.getId();
    user.email = profile.getEmail();
    return user;
  }
}
