import { fetchValidToken, checkToken, BASE_URL } from '../../utils/util'
const app = getApp();
Page({
  data: {
    token: "",
    img_logo: BASE_URL + "/public/images/logo.png",
  },

  onTokenChange(e: any) {
    const token = e.detail.value.trim();
    this.setData({"token": token,})
  },

  async sendRequest() {
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
      wx.showToast({title: '券码无效, 请联系客服！',duration: 3000, icon: 'none',});
      return;
    }
  },

  async onLoad() {
    
  },

  async onReady() {
    app.onOpenIdReady((openId) => {
      fetchValidToken("/public/audit/user-token", openId).then((resp)=> {
        if (resp.token !== "token") {
          wx.redirectTo({url: '/pages/entry-point/entry-point',});
        } 
      })
    })
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