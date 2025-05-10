import { fetchValidToken } from '../../utils/util'

Component({
  data: {
    motto: 'Hello World',
    
  },
  methods: {
    async toLogs() {
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
