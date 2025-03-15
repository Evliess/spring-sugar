Page({

  data: {
    answer: {},
    userInput: {},
    answerInString: "",
    showIndictor: false
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

  callApi(data: any) {
    const app = getApp();
    this.setData({"showIndictor": true});
    wx.request({
      url: 'http://localhost:8080/public/sugar',
      method: 'POST',
      data: data,
      header: {'content-type': 'application/json', 'X-TK': '123'},
      success: (res) => {
        this.data.answerInString = res.data.toString();
        const obj = app.towxml(
          res.data, 'markdown', {theme: 'light'}
        );
        this.setData({"answer": obj});
        this.setData({"showIndictor": false});
      },
      fail:()=> {
        this.setData({"showIndictor": false});
      },
    });
  },

  onLoad(options: any) {
    this.data.userInput = JSON.parse(decodeURIComponent(options.data))
    console.log(this.data.userInput)
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
})