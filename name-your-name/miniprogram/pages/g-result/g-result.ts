// pages/g-result/g-result.ts
Page({
  data: {
    answer: {},
  },

  onLoad() {
    const app = getApp();
    this.setData({
      answer: app.globalData.answer,
    });
    const obj = app.towxml(
      this.data.answer, 'markdown', {theme: 'light'}
    );
    this.setData({"answer": obj});
  },

  callApi(data: any) {
    const app = getApp();
    this.setData({"showIndictor": true});
    let openId = "";
    try {
      openId = wx.getStorageSync("openId");
    } catch(e) {}
    wx.request({
      url: 'http://localhost:8080/private/sugar',
      method: 'POST',
      data: data,
      header: {'content-type': 'application/json', 'X-token': this.data.token, 'X-openId': openId},
      success: (res) => {
        if (res.statusCode === 401) {
          this.setData({"showIndictor": false});
          wx.showToast({
            title: '券码无效！',
            duration: 1000
          });
          return;
        }
        app.globalData.answer = res.data.toString();
        this.data.answerInString = res.data.toString();
        const obj = app.towxml(
          res.data, 'markdown', {theme: 'light'}
        );
        this.setData({"answer": obj});
        this.setData({"showIndictor": false, "validToken": true});
      },
      fail:()=> {
        this.setData({"showIndictor": false, "validToken": false});
      }
    });
  },

  editInput() {},
  sendRequest(){},

  onReady() {},
  onShow() {},
})