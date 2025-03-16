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
    const token = e.detail.value;
    this.setData({
      "token": token,
    })
  },

  callApi(data: any) {
    const app = getApp();
    this.setData({"showIndictor": true});
    wx.request({
      url: 'http://localhost:8080/private/sugar',
      method: 'POST',
      data: data,
      header: {'content-type': 'application/json', 'X-token': this.data.token},
      success: (res) => {
        if (res.statusCode === 401) {
          this.setData({"showIndictor": false});
          wx.showToast({
            title: '券码无效！',
            duration: 1000
          });
          return;
        }
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

  onLoad(options: any) {
    this.data.userInput = JSON.parse(decodeURIComponent(options.data))
    console.log(this.data.userInput)
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
})