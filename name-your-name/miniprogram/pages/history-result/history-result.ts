Page({

  data: {
    answer: {},
    anchor: 'topAnchor',
  },

  onLoad() {
    const app = getApp();
    console.log(app.globalData.historyResp);
    this.setData({
      answer: app.globalData.historyResp,
    });
    const obj = app.towxml(
      this.data.answer, 'markdown', { theme: 'light' }
    );
    this.setData({ "answer": obj, "anchor": "topAnchor" });
  },

  onReady() {

  },

  onShow() {

  },

  onHide() {

  },

  onUnload() {

  },

  onPullDownRefresh() {

  },

  onReachBottom() {

  },

  onShareAppMessage() {

  }
})