import { Injectable } from '@angular/core';

import { HttpClient } from '@angular/common/http';
import { Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})

export class UserService {

  constructor(private http:HttpClient) { }
  
  // BASE_URL = 'http://localhost:8080';
  BASE_URL = 'https://www.tyty.wang';
  tokens: Array<string> = [];

  getToken(user: string, tokenLiveDays: string): Array<string> {
    const apiUrl = this.BASE_URL + '/private/tokens';
    const headers = {
      "X-Openid": user
    };
    const body = {
      "days": tokenLiveDays
    }
    this.http.post(apiUrl, body, {headers}).subscribe((data: any) => {
      this.tokens.push(data.token);
    });
    return this.tokens;
  }

  deactive(user: string, token: string): Observable<any> {
    const apiUrl = this.BASE_URL + '/private/token/deactive';
    const body = {"token": token};
    const headers = {
      "X-Openid": user
    };
    return this.http.post(apiUrl, body, {headers});
  }

  login(name: string, key: string): Observable<any> {
    const apiUrl = this.BASE_URL + '/public/user';
    const body = {
      "name": name,
      "key": key
    }
    return this.http.post(apiUrl, body);
  }

  getAuditInfo(timeSpan: string): Observable<any[]> {
    const apiUrl = this.BASE_URL + '/public/audit/time-span';
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

  insertNames(names: string, type: string): Observable<any> {
    if(type == "female") {
      const apiUrl = this.BASE_URL + '/public/dict/batch/female';
      return this.http.post(apiUrl, names);
    }
    if (type == "male") {
      const apiUrl = this.BASE_URL + '/public/dict/batch/male';
      return this.http.post(apiUrl, names);
    }
    if (type == "mid") {
      const apiUrl = this.BASE_URL + '/public/dict/batch/mid';
      return this.http.post(apiUrl, names);
    }
    return new Observable();
  }


}
