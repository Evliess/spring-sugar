import { fetchSugar, fetchValidToken } from '../../utils/util'

Component({
  data: {
    title_name: "名字",
    logs: [],
    userInput: {
      name: '',
      sex: '',
      mbti: '',
      meaning: '',
      other: '',
      voice: true
    },
    showIndictor: false,
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
      let validToken = false;
      let token = "";
      try {
        const resp = await fetchValidToken("/public/audit/user-token", app.globalData.openId);
        if (resp.token != "token") {
          validToken = true;
          token = resp.token;
        } else {
          validToken = false;
        }
      } catch(error) {
        validToken = false;
      }
      if (!validToken) {
        wx.navigateTo({url: '../custom-name/custom-name'})
      } else {
        this.setData({"showIndictor": true});
        const openId = app.globalData.openId;
        try {
          const resp = await fetchSugar("/private/sugar", openId, token, app.globalData.userInput);
          app.globalData.answer = resp;
          this.setData({"showIndictor": false});
          wx.navigateTo({url: '/pages/g-result/g-result',});
        } catch(e) {
          this.setData({"showIndictor": false});
          return;
        }
      }      
    },
  },
  lifetimes: {
    attached() {
      const app = getApp();
      const userInput = app.globalData.userInput;
      if (userInput && userInput.name != "") {
        this.setData({"userInput": app.globalData.userInput});
      }
    },
    detached() {},
  },
})
