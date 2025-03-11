// logs.ts
// const util = require('../../utils/util.js')
import { formatTime } from '../../utils/util'

Component({
  data: {
    logs: [],
  },
  methods: {
    toCustomName(){
      wx.navigateTo({
        url: '../custom-name/custom-name',
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
