// index.ts
// 获取应用实例
const app = getApp<IAppOption>()

Component({
  data: {
    motto: 'Hello World',
    
  },
  methods: {
    // 事件处理函数
    toLogs() {
      wx.navigateTo({
        url: '../logs/logs',
      })
    },
    toDict() {
      wx.navigateTo({
        url: '../dict-search/dict-search',
      })
    },
  },
})
