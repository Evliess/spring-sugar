Page({
  data: {
    answer: {},
    answerInString: "",
    name: "",
  },

  onNameChange(e: any) {
    const name = e.detail.value;
    this.setData({
      "name": name,
    })
  },
  sendRequest() {
    const app = getApp();
    const data = {"name": this.data.name};
    wx.request({
      url: 'http://localhost:8080/public/search/name',
      method: 'POST',
      data: data,
      success: (res) => {
        this.data.answerInString = res.data.toString();
        const obj = app.towxml(
          res.data, 'markdown', {theme: 'light'}
        );
        this.setData({"answer": obj});
      },
    });
  },

  onLoad() {

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