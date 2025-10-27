// pages/dict-choose/dict-choose.ts
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentIndex: 0,
    items: [{"name": "地道女生英文名", "img": "./images/avatar_1.png"},
    {"name": "海外男生英文名", "img": "./images/avatar_2.png"},
    {"name": "冷淡中性风英文名", "img": "./images/avatar_3.png"},
    {"name": "精选个性英文名", "img": "./images/avatar_4.png"},
    {"name": "美国热门英文名", "img": "./images/avatar_5.png"},
    {"name": "英国热门英文名", "img": "./images/avatar_6.png"},
    {"name": "加拿大热门英文名", "img": "./images/avatar_7.png"},
  ]
  },

  onItemTap(e:any){
    const index = e.currentTarget.dataset.index;
    this.setData({currentIndex: index});
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