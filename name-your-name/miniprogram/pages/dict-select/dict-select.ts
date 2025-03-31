// pages/dict-select/dict-select.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  onTapFemale(){
    const app = getApp();
    wx.request({
      url: 'http://localhost:8080/public/dict/type',
      method: 'POST',
      data: {"type": "female", 'X-Openid': app.globalData.openId},
      header: {'content-type': 'application/json'},
      success: (res) => {
        app.globalData.femaleNames = res.data.names;
        wx.navigateTo({
          url: "../dict-search/dict-search?type=female",
        })
      },
      fail:()=> {}
    });
  },
  onTapMale(){
    const app = getApp();
    wx.request({
      url: 'http://localhost:8080/public/dict/type',
      method: 'POST',
      data: {"type": "male", 'X-Openid': app.globalData.openId},
      header: {'content-type': 'application/json'},
      success: (res) => {
        app.globalData.maleNames = res.data.names;
        wx.navigateTo({
          url: "../dict-search/dict-search?type=male",
        })
      },
      fail:()=> {}
    });
  },
  onTapMid(){
    const app = getApp();
    wx.request({
      url: 'http://localhost:8080/public/dict/type',
      method: 'POST',
      data: {"type": "mid", 'X-Openid': app.globalData.openId},
      header: {'content-type': 'application/json'},
      success: (res) => {
        app.globalData.midNames = res.data.names;
        wx.navigateTo({
          url: "../dict-search/dict-search?type=mid",
        })
      },
      fail:()=> {}
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad() {

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