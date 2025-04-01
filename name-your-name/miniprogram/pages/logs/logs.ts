import { fetchValidToken } from '../../utils/util'

Component({
  data: {
    logs: [],
    userInput: {
      name: '',
      sex: '',
      mbti: '',
      meaning: '',
      other: '',
      voice: true
    }
  },
  methods: {
    onNameChange(e: any) {
      const name = e.detail.value;
      if (name !== null && name.length > 0) {
        this.setData({
          "userInput.name": name,
        })
      }
    },
    onSexChange(e: any) {
      const sex = e.detail.value;
      this.setData({
          "userInput.sex": sex,
      })
    },
    onStarChange(e: any) {
      const star = e.detail.value;
      this.setData({
        "userInput.mbti": star,
      })
    },
    onMeaningChange(e: any) {
      const meaning = e.detail.value;
      this.setData({
        "userInput.meaning": meaning,
      })
    },

    onOtherChange(e: any) {
      const other = e.detail.value;
       this.setData({
        "userInput.other": other,
      })
    },

    onVoiceChange(e: any){
      const voice = e.detail.value;
      this.setData({
        "userInput.voice": voice,
      })
    },

    async toCustomName(){
      const app = getApp();
      app.globalData.userInput = this.data.userInput;
      const openId = app.globalData.openId;
      try {
        const resp = await fetchValidToken("/public/audit/user-token", openId);
        if (resp.token != "token") {
          app.globalData.token = resp.token;
          app.globalData.validToken = true;
        }
      } catch(error) {}
      wx.navigateTo({url: '../custom-name/custom-name'})
    },
  },
  lifetimes: {
    attached() {
      const app = getApp();
      const userInput = app.globalData.userInput;
      if (userInput.name != "") {
        this.setData({"userInput": app.globalData.userInput});
      }
    },
    detached() {},
  },
})
