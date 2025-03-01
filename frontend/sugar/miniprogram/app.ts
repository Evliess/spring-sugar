// app.ts


App({
  globalData: {},
  towxml: require('/towxml/index'),
  onLaunch() {
    // 展示本地存储能力
    const logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now());
    wx.setStorageSync('logs', logs);
    wx.loadFontFace({
      family: 'SourceHanSerifCN-Regular',
      global: true,
      source: 'url("http://localhost:8080/SourceHanSerifSC-Light.otf")',
    });
    

    // 登录
    wx.login({
      success: res => {
        console.log(res.code)
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      },
    })
  },
})