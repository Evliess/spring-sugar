import { Component } from '@angular/core';
import { CommonModule } from "@angular/common";
import { UserLoginComponent } from '../user-login/user-login.component';
import { UserService } from '../../services/user.service';
import { NzListModule } from 'ng-zorro-antd/list';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzNotificationService } from 'ng-zorro-antd/notification';


@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css',
  imports: [UserLoginComponent, CommonModule, NzListModule, NzButtonModule],
})
export class WelcomeComponent {
  isLogin: boolean = false;
  tokens: Array<string> = [];

  constructor(private userService: UserService
    , private notification: NzNotificationService
  ) {}

  loginEvent(event: boolean): void {
    this.isLogin = event; 
  }

  getToken(): void {
    this.tokens = this.userService.getToken();
    console.log(this.tokens);
  }

  copy(token: string): void {
    navigator.clipboard.writeText(token).then(() => {
      this.notification.success('复制成功', `已复制: ${token}`, {
        nzPlacement: 'bottomRight' // 设置提示位置为右下角
      });
    }).catch(err => {
      this.notification.error('复制失败', `已复制: ${token}`, {
        nzPlacement: 'bottomRight' // 设置提示位置为右下角
      });
    });
  }
  
}
