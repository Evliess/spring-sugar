import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GlobalService {
  private logoutClickSource = new Subject<string>();
  buttonClick$ = this.logoutClickSource.asObservable();

  publishButtonClick(message: string): void {
    this.logoutClickSource.next(message);
  }
}