import { fetchValidToken } from '../../utils/util'

Component({
  data: {
    motto: 'Hello World',
  },
  methods: {
    async toLogs() {
      const app = getApp();
      const resp = await fetchValidToken("/public/audit/user-token", app.globalData.openId);
      if (resp.token !== "token") {
        wx.navigateTo({
          url: '/pages/logs/logs',
        });
      } else {
        wx.navigateTo({
          url: '../custom-name/custom-name',
        })
      }
    },
    toDict() {
      wx.navigateTo({
        url: '../dict-select/dict-select',
      })
    },
  },
})
