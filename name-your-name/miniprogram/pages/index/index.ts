const app = getApp<IAppOption>()

Component({
  data: {
    motto: 'Hello World',
    
  },
  methods: {
    toLogs() {
      wx.navigateTo({
        url: '../logs/logs',
      })
    },
    toDict() {
      wx.navigateTo({
        url: '../dict-select/dict-select',
      })
    },
  },
})
