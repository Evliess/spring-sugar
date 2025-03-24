// pages/g-result/g-result.ts
Page({
  data: {
    answer: {},
    answerInString: '',
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
    const token = app.globalData.token;
    const openId = app.globalData.openId;
    this.setData({"showIndictor": true});
    wx.request({
      url: 'http://localhost:8080/private/sugar',
      method: 'POST',
      data: data,
      header: {'content-type': 'application/json', 'X-token': token, 'X-openId': openId},
      success: (res) => {
        if (res.statusCode === 401) {
          this.setData({"showIndictor": false});
          wx.showToast({
            title: '券码无效！',
            duration: 1000
          });
          wx.navigateTo({
            url: '/pages/custom-name/custom-name',
          })
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

  editInput() {
    wx.navigateTo({
      url: '/pages/logs/logs',
    })
  },
  sendRequest(){
    const app = getApp();
    const data = app.globalData.userInput;
    this.callApi(data);
  },

  onReady() {},
  onShow() {},
})