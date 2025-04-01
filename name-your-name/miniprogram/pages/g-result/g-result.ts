import { fetchSugar } from '../../utils/util'
Page({
  data: {
    answer: {},
    showIndictor: false,
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

  editInput() {
    wx.redirectTo({
      url: '/pages/logs/logs',
    })
  },
  async sendRequest(){
    this.setData({"showIndictor": true});
    const app = getApp();
    const userInput = app.globalData.userInput;
    const token = app.globalData.token;
    const openId = app.globalData.openId;
    try {
      this.setData({"showIndictor": false});
      const resp = await fetchSugar("/private/sugar", openId, token, userInput);
      app.globalData.answer = resp;
      const obj = app.towxml(resp, 'markdown', {theme: 'light'});
      this.setData({"answer": obj});
    } catch(e) {
        wx.showToast({title: '券码无效！', duration: 1000});
        this.setData({"showIndictor": false});
        wx.redirectTo({
          url: '/pages/custom-name/custom-name',
        })
        app.globalData.validToken = false;
        return;
    }
  },

  onReady() {},
  onShow() {},
})