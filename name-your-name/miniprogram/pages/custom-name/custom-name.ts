Page({

  data: {
    answer: {},
    userInput: {},
    answerInString: "",
    showIndictor: false,
    token: "",
    validToken: false
  },

  copyResponse() {
    wx.setClipboardData({
      data: this.data.answerInString,
      success: ()=> {
        wx.showToast({
          title: '复制成功',
          duration: 1000
        });
      },
      fail: ()=> {
        wx.showToast({
          title: '复制失败',
          duration: 1000
        });
      },
    });
  },

  sendRequest() {
    this.callApi(this.data.userInput);
  },

  onTokenChange(e: any) {
    const token = e.detail.value.trim();
    this.setData({
      "token": token,
    })
  },

  callApi(data: any) {
    const app = getApp();
    this.setData({"showIndictor": true});
    let openId = "";
    try {
      openId = app.globalData.openId;
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
        app.globalData.token = this.data.token;
        wx.navigateTo({
            url: '/pages/g-result/g-result',
          });
        this.setData({"showIndictor": false, "validToken": true});
      },
      fail:()=> {
        this.setData({"showIndictor": false, "validToken": false});
      }
    });
  },

  onLoad() {
    const app = getApp();
    this.data.userInput = app.globalData.userInput;
    const token = app.globalData.token;
    if(token.length >0) {
      this.setData({"token": token, "validToken": true});
    }
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
})