import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  tokens: Array<string> = [];

  getToken(): Array<string> {
    const apiUrl = 'http://localhost:8080/public/tokens';
    const body = {
      "name": 'John Doe',
    }
    this.http.post(apiUrl, body).subscribe((data: any) => {
      console.log(data);
      this.tokens.push(data.token);
    });
    return this.tokens;
  }
}
