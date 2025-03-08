import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http:HttpClient) { }

  tokens: Array<string> = [];

  getToken(user: string): Array<string> {
    const apiUrl = 'http://localhost:8080/public/tokens';
    const body = {
      "days": 5
    }
    this.http.post(apiUrl, body).subscribe((data: any) => {
      console.log(data);
      this.tokens.push(data.token);
    });
    return this.tokens;
  }


  login(name: string, key: string): Observable<any> {
    const apiUrl = 'http://localhost:8080/public/user';
    const body = {
      "name": name,
      "key": key
    }
    return this.http.post(apiUrl, body);
  }
}
