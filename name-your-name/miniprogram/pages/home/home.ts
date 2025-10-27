import { fetchValidToken, checkToken } from '../../utils/util'
Page({

  data: {
    token: ""
  },

  onTokenChange(e: any) {
    const token = e.detail.value.trim();
    this.setData({"token": token,})
  },

  async sendRequest() {
    const app = getApp();
    let openId = "";
    try {
      openId = app.globalData.openId;
    } catch(e) {}
    try {
      const resp = await checkToken("/private/audit/check-token", openId, 
      this.data.token);
      if(resp.result=="ok") {
        wx.navigateTo({url: '/pages/entry-point/entry-point',});
      } else {
        wx.showToast({title: '券码无效, 联系客服！',duration: 3000, icon: 'none',});
      }
    } catch(e) {
      wx.showToast({title: '券码无效, 联系客服！',duration: 3000, icon: 'none',});
      return;
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  async onLoad() {
    const app = getApp();
    const resp = await fetchValidToken("/public/audit/user-token", app.globalData.openId);
    //bbb36c95-7d10-49ff-a5ec-df262ff51d25--BHHGFJIHIFIAI
    resp.token = "token" //测试代码 
    if (resp.token !== "token") {
      wx.navigateTo({
        url: '/pages/entry-point/entry-point',
      });
    } 
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage() {

  }
})