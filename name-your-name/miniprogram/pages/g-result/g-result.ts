import { fetchSugar, fetchValidToken } from '../../utils/util'
Page({
  data: {
    answer: {},
    showIndictor: false,
    anchor: 'topAnchor',
  },

  onLoad() {
    const app = getApp();
    this.setData({
      answer: app.globalData.answer,
    });
    const obj = app.towxml(
      this.data.answer, 'markdown', {theme: 'light'}
    );
    this.setData({"answer": obj, "anchor": "topAnchor"});
  },

  editInput() {
    wx.redirectTo({
      url: '/pages/logs/logs',
    })
  },
  async sendRequest(){
    this.setData({"showIndictor": true});
    const loadingIcon = this.selectComponent("#loadingIcon");
    loadingIcon.startLoading();
    const app = getApp();
    const userInput = app.globalData.userInput;
    let token = "";
    const openId = app.globalData.openId;
    try {
      const resp = await fetchValidToken("/public/audit/user-token", openId);
      if (resp.token != "token") {
        token = resp.token;
      } 
    } catch(error) {
      token = "";
    }
    try {
      const resp = await fetchSugar("/private/sugar", openId, token, userInput);
      app.globalData.answer = resp;
      const obj = app.towxml(resp, 'markdown', {theme: 'light'});
      this.setData({"answer": obj, "anchor": "topAnchor", "showIndictor": false});
      loadingIcon.stopLoading();
    } catch(e) {
        wx.showToast({title: '券码无效！', duration: 1000});
        this.setData({"showIndictor": false});
        loadingIcon.stopLoading();
        wx.redirectTo({
          url: '/pages/custom-name/custom-name',
        })
        return;
    }
  },

  onReady() {},
  onShow() {},
})