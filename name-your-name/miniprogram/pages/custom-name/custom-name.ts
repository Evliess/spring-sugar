import { checkToken } from '../../utils/util'
Page({
  data: {
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
      const resp = await checkToken("/private/audit/check-token", openId, this.data.token);
      wx.navigateTo({
          url: '/pages/logs/logs',
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
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
})