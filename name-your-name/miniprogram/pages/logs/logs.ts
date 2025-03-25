// logs.ts
// const util = require('../../utils/util.js')
import { formatTime } from '../../utils/util'

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
        //todo check name > 20
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
      // check meaning length
      this.setData({
        "userInput.meaning": meaning,
      })
    },

    onOtherChange(e: any) {
      const other = e.detail.value;
       // check other length
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

    toCustomName(){
      const app = getApp();
      app.globalData.userInput = this.data.userInput;
      //isValidToken
      const openId = app.globalData.openId;
      wx.request({
        url: 'http://localhost:8080/public/audit/user-token',
        method: 'POST',
        data: {"openId": openId},
        header: {'content-type': 'application/json'},
        success: (res: any) => {
          if(res.data != "token") {
            app.globalData.token = res.data.token;
          }
        },
        fail:()=> {
         
        }
      });
      wx.navigateTo({
        url: '../custom-name/custom-name'
      })
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
