import { Component, OnInit } from '@angular/core';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzGridModule } from 'ng-zorro-antd/grid';
import { NzCardModule } from 'ng-zorro-antd/card';
import { FormsModule } from '@angular/forms';
import { NzDividerModule } from 'ng-zorro-antd/divider';
import { NzTableModule, NzTableSortFn, NzTableSortOrder} from 'ng-zorro-antd/table';
import { NzIconModule } from 'ng-zorro-antd/icon';



import { CommonModule } from "@angular/common";
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';

interface Option {
  label: string;
  value: string;
}

interface UserInfo {
  user: string;
  
}

interface ColumnItem {
  name: string;
  sortOrder: NzTableSortOrder | null;
  sortFn: NzTableSortFn<string> | null;

}

@Component({
  selector: 'app-monitoring',
  imports: [NzSelectModule, FormsModule, CommonModule, NzGridModule, NzCardModule, NzDividerModule, NzTableModule, NzIconModule],
  templateUrl: './monitoring.component.html',
  styleUrl: './monitoring.component.css'
})
export class MonitoringComponent implements OnInit{

  constructor(public userService: UserService) { }

  selectedItem: Option = { label: '最近三天', value: 'latest_3d' };
  items: Option[] = [
    { label: '最近24时', value: 'latest_24h' }, 
    { label: '最近三天', value: 'latest_3d' }, 
    { label: '最近一周', value: 'latest_7d' },
    // { label: '最近一月', value: 'latest_30d' }
  ];

  listOfColumns: ColumnItem[] = [
    {
      name: "用户",
      sortOrder: null,
      sortFn: (a: any, b: any) => a.user.localeCompare(b.user)
    },
    {
      name: "类型",
      sortOrder: null,
      sortFn: (a: any, b: any) => a.type.localeCompare(b.type)
    },
    {
      name: "访问时间",
      sortOrder: null,
      sortFn: (a: any, b: any) => a.consumedAt - b.consumedAt,
    },
  ]

  uniqueUsers = 0;
  llmTotal = 0;
  requestTotal = 0;
  dictTotal = 0;
  compareFn = (o1: Option | null, o2: Option | null) => (o1 && o2 ? o1.value === o2.value : o1 === o2);
  userInfos: any[] = [];
  loading = true;

  ngOnInit(): void {
    this.userService.getAuditInfo("latest_3d").subscribe((data: any) => {
      this.userInfos = data;
      this.loading = false;
      this.uniqueUsers = new Set(data.map((item: any)=>item.user)).size;
      this.llmTotal = data.filter((item: any)=> item.type == "llm").length;
      this.requestTotal = data.length;
      this.dictTotal = this.requestTotal - this.llmTotal;
    });
  }

  onTimeSpanChange(item: Option): void {
    this.userService.getAuditInfo(item.value).subscribe((data: any) => {
      this.userInfos = data;
      this.loading = false;
      this.uniqueUsers = new Set(data.map((item: any)=>item.user)).size;
      this.llmTotal = data.filter((item: any)=> item.type == "llm").length;
      this.requestTotal = data.length;
      this.dictTotal = this.requestTotal - this.llmTotal;
    });
  }
  
  


  

}
