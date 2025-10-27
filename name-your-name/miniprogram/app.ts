// app.ts
import { BASE_URL } from './utils/util'
App({
  globalData: {
    answer: {},
    openId: "",
    openIdCallbacks: [],
    userInput: {
      name: '',
      sex: '',
      mbti: '',
      meaning: '',
      other: '',
      voice: true
    },
    midNames: [],
    femaleNames: [],
    maleNames:[]
  },
  towxml: require('/towxml/index'),
  BASE_URL: BASE_URL,
  onLaunch() {
    this.getOpenId();
  },

  getOpenId() {
    wx.login({
      success: (res) => {
        wx.request({
          url: this.BASE_URL + '/public/uid',
          method: 'POST',
          data: {"code": res.code},
          header: {'content-type': 'application/json'},
          success: (res) => {
            this.globalData.openId = res.data.openId;
            // 执行所有等待openId的回调
            this.globalData.openIdCallbacks.forEach(callback => {
              callback(this.globalData.openId)
            })
            this.globalData.openIdCallbacks = []
          }
        })
      }
    })
  },

  // 注册openId就绪回调
  onOpenIdReady(callback: any) {
    if (this.globalData.openId) {
      callback(this.globalData.openId)
    } else {
      this.globalData.openIdCallbacks.push(callback)
    }
  }
})