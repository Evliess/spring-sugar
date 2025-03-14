// logs.ts
// const util = require('../../utils/util.js')
import { formatTime } from '../../utils/util'

Component({
  data: {
    logs: [],
    userInput: {
      name: '',
      sex: '',
      star: '',
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
      // check star length
      this.setData({
        "userInput.star": star,
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
      wx.navigateTo({
        url: '../custom-name/custom-name?data=' + encodeURIComponent(JSON.stringify(this.data.userInput)),
      })
    },
  },
  lifetimes: {
    attached() {
    },
    detached() {
      console.log("leave...");
    },
  },
})
