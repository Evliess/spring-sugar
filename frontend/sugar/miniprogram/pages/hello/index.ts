// index.ts
// 获取应用实例


const util = require('../../utils/util.js')
const app = getApp();
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Component({
  data: {
    motto: '童言童语',
    userInfo: {
      avatarUrl: defaultAvatarUrl,
      nickName: '',
    },
    token: '',
    userInput: {
      name: '',
      sex: '',
      birthday: '',
      birthTime: '',
    },
    answer: {},
    hasUserInfo: false,
    canIUseGetUserProfile: wx.canIUse('getUserProfile'),
    canIUseNicknameComp: wx.canIUse('input.type.nickname'),
    sexs: [
      { value: 0, text: '男' },
      { value: 1, text: '女' }
    ],
    showIndictor: false,
    eatSugar: false,
  },
  methods: {
    // 事件处理函数
    bindViewTap() {
      // wx.navigateTo({
      //   url: '../logs/logs',
      // })
    },
    onChooseAvatar(e: any) {
      const { avatarUrl } = e.detail;
      const { nickName } = this.data.userInfo;
      this.setData({
        "userInfo.avatarUrl": avatarUrl,
        hasUserInfo: nickName && avatarUrl && avatarUrl !== defaultAvatarUrl,
      })
    },
    onNameChange(e: any) {
      const name = e.detail.value;
      if (name !== null && name.length > 0) {
        this.setData({
          "userInput.name": name,
        })
      }
    },
    onBirthdayChange(e: any) {
      const birthday = e.detail.value;
      this.setData({
        "userInput.birthday": birthday,
      })
    },

    onBirthTimeChange(e: any) {
      const birthTime = e.detail.value;
      this.setData({
        "userInput.birthTime": birthTime,
      })
    },

    onSexChange(e: any) {
      const sex = e.detail.value;
      this.setData({
        "userInput.sex": sex,
      })
    },

    checkName() {
      const regex = /^[\u4e00-\u9fa5]{2,15}(·[\u4e00-\u9fa5]{2,15})?$/;
      return regex.test(this.data.userInput.name);
    },

    checkSex() {
      return this.data.userInput.sex !== '';
    },

    checkBirthday() {
      const regex = /^(19\d{2}|20\d{2})-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;
      const dateString = this.data.userInput.birthday + "";
      if (!regex.test(dateString)) {
        return false;
      }
      const [year, month, day] = dateString.split('-').map(Number);
      const isLeapYear = (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
      const daysInMonth = [
        31, isLeapYear ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
      ];
      return day <= daysInMonth[month - 1];
    },

    checkTime() {
      const timeString = this.data.userInput.birthTime;
      const regex = /^([01]\d|2[0-3]):([0-5]\d)$/;
      return regex.test(timeString);
    },

    showToast(msg: any) {
      wx.showToast({
        title: msg,
        icon: 'none',
        duration: 1500
      });
    },

    showTokenModal() {
      wx.showModal({
        placeholderText: '请输入口令',
        editable: true,
        success: (res) => {
          if (res.confirm) {
            this.setData({
              "token": res.content
            });
            const userInput = this.buildData();
            this.sendReq(userInput);
            this.setData({ "eatSugar": true, "showIndictor": true, "answer": {} });
          }
        }
      });

    },

    checkUserInput() {
      if (!this.checkName()) {
        this.showToast("请输入名字!");
        return false;
      }
      if (!this.checkSex()) {
        this.showToast("请选择性别!");
        return false;
      }
      if (!this.checkBirthday()) {
        this.showToast("日期格式不对！");
        return false;
      }
      if (!this.checkTime()) {
        this.showToast("时间格式不对！");
        return false;
      }
      return true;
    },

    buildData() {
      const data: any = {}
      data.name = this.data.userInput.name;
      data.sex = this.data.userInput.sex == '0' ? '男' : '女';
      const dateString = this.data.userInput.birthday;
      const [year, month, day] = dateString.split('-').map(Number);
      const timeString = this.data.userInput.birthTime;
      const [hour, minute] = timeString.split(':').map(Number);
      data.birth = year + '年' + month + '月' + day + '日 ' + hour + '时';
      return data;
    },

    handleContact(data: any) {
      
    },

    getPhoneNumber(e: any) {
      console.log(e.detail.code);
      console.log(e.detail.errMsg);
      console.log(e.detail.errno);

      

    },

    sendReq(data: any) {
      wx.request({
        // url: 'http://localhost:8080/public/sugar',
        url: 'https://www.tyty.wang/public/sugar',
        method: 'POST',
        data: data,
        header: {
          'content-type': 'application/json'
        },
        success: (res) => {
          const obj = app.towxml(res.data, 'markdown', { theme: 'light' });
          this.setData({
            "answer": obj,
          })
          this.setData({ "eatSugar": false, "showIndictor": false });
        },
        fail: () => {
          this.setData({ "eatSugar": false, "showIndictor": false, "answer":{} });
        },
      })

    },

    checkToken() {
      if (this.data.token == "1111") return true;
      this.showToast("你的口令无效，请联系管理员")
      return false;
    },

    onSubmit(e: any) {
      if (this.checkUserInput()) {
        this.showTokenModal();

      }


    },
    getUserProfile() {
      // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
      wx.getUserProfile({
        desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
        success: (res) => {
          this.setData({
            hasUserInfo: true
          })
        }
      })
    },
  },
})
