// app.ts
App({
  globalData: {
    answer: {},
    token: "",
    openId: "",
    validToken: false,
    userInput: {
      name: '',
      sex: '',
      mbti: '',
      meaning: '',
      other: '',
      voice: true
    }
  },
  towxml: require('/towxml/index'),
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        wx.request({
          url: 'http://localhost:8080/public/uid',
          method: 'POST',
          data: {"code": res.code},
          header: {'content-type': 'application/json'},
          success: (res: any) => {
            this.globalData.openId = res.data.openId;
          },
          fail:()=> {
           
          }
        });
      },
    })
  },
})