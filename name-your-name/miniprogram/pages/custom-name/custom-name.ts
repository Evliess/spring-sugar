import { fetchSugar } from '../../utils/util'
Page({
  data: {
    answer: {},
    userInput: {},
    showIndictor: false,
    token: "",
    validToken: false
  },

  async sendRequest() {
    const app = getApp();
    this.setData({"showIndictor": true});
    let openId = "";
    try {
      openId = app.globalData.openId;
    } catch(e) {}
    try {
      const resp = await fetchSugar("/private/sugar", openId, this.data.token, this.data.userInput);
      app.globalData.answer = resp;
      app.globalData.token = this.data.token;
      app.globalData.validToken = true;
      wx.navigateTo({
          url: '/pages/g-result/g-result',
        });
      this.setData({"showIndictor": false, "validToken": true});
    } catch(e) {
      this.setData({"showIndictor": false, "validToken": false});
      wx.showToast({
        title: '券码无效！',
        duration: 1000
      });
      return;
    }
  },

  onTokenChange(e: any) {
    const token = e.detail.value.trim();
    this.setData({
      "token": token,
    })
  },

  onLoad() {
    const app = getApp();
    this.data.userInput = app.globalData.userInput;
    const token = app.globalData.token;
    if(app.globalData.validToken) {
      this.setData({"token": token, "validToken": true});
    }
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
})