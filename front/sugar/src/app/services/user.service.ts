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

  getAuditInfo(timeSpan: string): Observable<any[]> {
    const apiUrl = 'http://localhost:8080/public/audit/time-span';
    const body = {
      "timeSpan": timeSpan
    }
    return this.http.post<any[]>(apiUrl, body);
  }


  padZero(num: number): string {
    return num < 10 ? `0${num}` : num.toString();
  }

  formatDate(timestamp: number): string {
    const date = new Date(timestamp);
    const year = date.getFullYear();
    const month = this.padZero(date.getMonth() + 1);
    const day = this.padZero(date.getDate());
    const hours = this.padZero(date.getHours());
    const minutes = this.padZero(date.getMinutes());
    const seconds = this.padZero(date.getSeconds());

    return `${year}/${month}/${day} ${hours}:${minutes}:${seconds}`;
  }
}
