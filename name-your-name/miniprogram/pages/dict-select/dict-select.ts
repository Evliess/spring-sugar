import {fetchDictNamesByType} from '../../utils/util'

Page({
  data: {},

  async onTapFemale() {
    const app = getApp();
    try {
      const data = await fetchDictNamesByType("/public/dict/type", "female", app.globalData.openId);
      app.globalData.femaleNames = data.names;
      wx.navigateTo({url: "../dict-search/dict-search?type=female"})
    } catch(error) {console.error("failed");}
  },

  async onTapMale(){
    const app = getApp();
    try {
      const data = await fetchDictNamesByType("/public/dict/type", "male", app.globalData.openId);
      app.globalData.femaleNames = data.names;
      wx.navigateTo({url: "../dict-search/dict-search?type=female"})
    } catch(error) {console.error("failed");}
  },
  async onTapMid(){
    const app = getApp();
    try {
      const data = await fetchDictNamesByType("/public/dict/type", "mid", app.globalData.openId);
      app.globalData.femaleNames = data.names;
      wx.navigateTo({url: "../dict-search/dict-search?type=female"})
    } catch(error) {console.error("failed");}
  },

  onLoad() {},
  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
  onPullDownRefresh() {},
  onReachBottom() {},
  onShareAppMessage() {}
})