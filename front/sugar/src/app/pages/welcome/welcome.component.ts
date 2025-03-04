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
  user: string = '';

  constructor(
    private userService: UserService,
    private notification: NzNotificationService
  ) { }

  loginEvent(event: string): void {
    if (event.length > 0) {
      this.user = event;
      this.isLogin = true;
    }
  }

  getToken(): void {
    this.tokens = this.userService.getToken(this.user);
  }

  copy(token: string): void {
    navigator.clipboard.writeText(token).then(() => {
      this.notification.success('复制成功', `已复制: ${token}`, {
        nzPlacement: 'bottomRight'
      });
    }).catch(err => {
      this.notification.error('复制失败', `已复制: ${token}`, {
        nzPlacement: 'bottomRight'
      });
    });
  }

}
