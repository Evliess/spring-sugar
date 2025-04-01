import { fetchValidToken } from '../../utils/util'

Component({
  data: {
    motto: 'Hello World',
    
  },
  methods: {
    async toLogs() {
      const app = getApp();
      const openId = app.globalData.openId;
      try {
        const resp = await fetchValidToken("/public/audit/user-token", openId);
        if (resp.token != "token") {
          app.globalData.token = resp.token;
          app.globalData.validToken = true;
        }
      } catch(error) {}
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
