import {fetchDictNamesByType} from '../../utils/util'
Page({

  data: {
    currentIndex: -1,
    items: [{"name": "地道女生英文名", "img": "./images/avatar_1.png", "action": "onTapFemale"},
    {"name": "海外男生英文名", "img": "./images/avatar_2.png", "action": "onTapMale"},
    {"name": "冷淡中性风英文名", "img": "./images/avatar_3.png", "action": "onTapMid"},
    {"name": "精选个性英文名", "img": "./images/avatar_4.png"},
    {"name": "美国热门英文名", "img": "./images/avatar_5.png"},
    {"name": "英国热门英文名", "img": "./images/avatar_6.png"},
    {"name": "加拿大热门英文名", "img": "./images/avatar_7.png"},
  ]
  },

  onItemTap(e:any){
    const index = e.currentTarget.dataset.index;
    this.setData({currentIndex: index});
    const name = e.currentTarget.dataset.name;
    if(name == "地道女生英文名") {
      this.onTapFemale();
    } else if(name == "海外男生英文名") {
      this.onTapMale();
    } else if(name == "冷淡中性风英文名") {
      this.onTapMid();
    } else {
      return;
    }
  },


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