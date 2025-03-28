import { Component, inject,Output, EventEmitter } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'user-login',
  imports: [ReactiveFormsModule, NzButtonModule, NzFormModule, NzInputModule],
  template: `
    <form nz-form [nzLayout]="'inline'" [formGroup]="validateForm" (ngSubmit)="submitForm()">
      <nz-form-item>
        <nz-form-control nzErrorTip="Please input your username!">
          <nz-input-group nzPrefixIcon="user">
            <input formControlName="username" nz-input placeholder="username" />
          </nz-input-group>
        </nz-form-control>
      </nz-form-item>
      <nz-form-item>
        <nz-form-control nzErrorTip="Please input your Password!">
          <nz-input-group nzPrefixIcon="lock">
            <input formControlName="password" nz-input type="password" placeholder="Password" />
          </nz-input-group>
        </nz-form-control>
      </nz-form-item>
      <nz-form-item>
        <nz-form-control>
          <button nz-button nzType="primary" [disabled]="!validateForm.valid">Log in</button>
        </nz-form-control>
      </nz-form-item>
    </form>
  `
})
export class UserLoginComponent {
  private fb = inject(NonNullableFormBuilder);
  validateForm = this.fb.group({
    username: this.fb.control('', [Validators.required]),
    password: this.fb.control('', [Validators.required]),
    remember: this.fb.control(true)
  });

  constructor(private userService: UserService) {

  }

  @Output() loginEvent = new EventEmitter<string>();

  submitForm(): void {
    if (this.validateForm.value.username == undefined || this.validateForm.value.password == undefined) {
      return;
    }
    let username = this.validateForm.value.username;
    this.userService.login(this.validateForm.value.username, this.validateForm.value.password).subscribe((data: any) => {
      let token = data.token;
      if (token !== "F") {
        localStorage.setItem('user', username);
        this.loginEvent.emit(username);
      } else {
        this.loginEvent.emit("");
      }
    });
    
  }
}