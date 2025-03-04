import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }

  token: Array<string> = ["123", "456", "789"];

  getToken(): Array<string> {
    return this.token;
  }
}
