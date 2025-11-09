import {fetchDictNamesByType, BASE_URL} from '../../utils/util'
Page({

  data: {
    currentIndex: -1,
    items: [
    {"name": "地道女生英文名", "img": BASE_URL + "/public/images/avatar_1.png", "type": "female"},
    {"name": "海外男生英文名", "img": BASE_URL + "/public/images/avatar_2.png", "type": "male"},
    {"name": "冷淡中性风英文名", "img": BASE_URL + "/public/images/avatar_3.png", "type": "mid"},
    {"name": "精选个性英文名", "img": BASE_URL + "/public/images/avatar_4.png", "type": "jxgxywm"},
    {"name": "美国热门英文名", "img": BASE_URL + "/public/images/avatar_5.png", "type": "mgrmywm"},
    {"name": "英国热门英文名", "img": BASE_URL + "/public/images/avatar_6.png", "type": "ygrmywm"},
    {"name": "加拿大热门英文名", "img": BASE_URL + "/public/images/avatar_7.png", "type": "jndrmywm"},
  ]
  },

  async onItemTap(e:any){
    const index = e.currentTarget.dataset.index;
    this.setData({currentIndex: index});
    const type = e.currentTarget.dataset.type;
    const app = getApp();
    try {
      const data = await fetchDictNamesByType("/public/dict/type", type, app.globalData.openId);
      if(data && data.names) {
        app.globalData.dictNames = data.names;
        wx.navigateTo({url: "../dict-search/dict-search"})
      } 
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