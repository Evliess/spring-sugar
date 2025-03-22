import { Component, OnInit } from '@angular/core';
import { CommonModule } from "@angular/common";
import { FormsModule } from '@angular/forms';
import { UserLoginComponent } from '../user-login/user-login.component';
import { UserService } from '../../services/user.service';
import { NzListModule } from 'ng-zorro-antd/list';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzNotificationService } from 'ng-zorro-antd/notification';
import { Observable, Subscription } from 'rxjs';
import { GlobalService } from '../../services/global.service'
import { NzFlexModule } from 'ng-zorro-antd/flex';



@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css',
  imports: [UserLoginComponent, CommonModule, NzListModule, NzButtonModule, NzInputModule, FormsModule],
})
export class WelcomeComponent implements OnInit {
  isLogin: boolean = false;
  tokens: Array<string> = [];
  user: string = '';
  logoutSubscription: Subscription | undefined;
  deactiveToken: string = '';
  tokenLiveDays: string = '5';

  constructor(
    private userService: UserService,
    private notification: NzNotificationService,
    private globalSvc:GlobalService
  ) { }

  ngOnInit(): void {
    this.isLogin = localStorage.getItem("user") != null;
    this.logoutSubscription = this.globalSvc.buttonClick$.subscribe((logout)=> {
      if (logout == "logout") this.isLogin = false;
    });
  }

  loginEvent(event: string): void {
    if (event.length > 0) {
      this.user = event;
      this.isLogin = true;
    }
  }

  getToken(): void {
    this.tokens = this.userService.getToken(this.user, this.tokenLiveDays);
  }

  onDeactiveTokenChange(event: any): void {
    this.deactiveToken = event.target.value;
  }

  onTokenLiveDaysChange(event: any): void {
    this.tokenLiveDays = event.target.value;
  }

  deactive(): void {
    this.userService.deactive(this.deactiveToken).subscribe((data: any) => {
      if(data.msg == "deactived") {
        this.notification.success('停用成功', `已停用: ${this.deactiveToken}`, {
          nzPlacement: 'bottomRight'
        });
      }
    });
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
