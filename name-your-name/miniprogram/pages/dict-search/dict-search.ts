Page({
  data: {
    answer: {},
    answerInString: "",
    name: "",
    filterdNames:[],
    allNames:[],
  },

  onNameChange(e: any) {
    const name = e.detail.value;
    this.setData({
      "name": name,
    })
  },
  sendRequest() {
    const app = getApp();
    let keyword = this.data.name.toLowerCase();
    const filterdNames = this.data.allNames.filter(item => {
      return item.name.toLowerCase().includes(keyword)}
    );
    let names = "";
    filterdNames.forEach((item) => {
      names = names + "- **" + item.name + "**: " + item.meaning + "\n";
    });
    const obj = app.towxml(names, 'markdown', {theme: 'light'});
    this.setData({"answer": obj});
  },

  onLoad(options) {
    const app = getApp();
    const type = options.type;
    let names: string = "";
    if(type == "female") {
      this.setData({"allNames": app.globalData.femaleNames});
      this.data.allNames.forEach((item) => {
        names = names + "- **" + item.name + "**: " + item.meaning + "\n";
      });
     
    } else if(type == "male") {
      this.setData({"allNames": app.globalData.maleNames});
      this.data.allNames.forEach((item) => {
        names = names + "- **" + item.name + "**: " + item.meaning + "\n";
      });
    } else {
      this.setData({"allNames": app.globalData.midNames});
      this.data.allNames.forEach((item) => {
        names = names + "- **" + item.name + "**: " + item.meaning + "\n";
      });
    }
    const obj = app.towxml(
      names, 'markdown', {theme: 'light'}
    );
    this.setData({"answer": obj});
  },

  onReady() {

  },

  onShow() {

  },


  onHide() {

  },


  onUnload() {

  },


  onPullDownRefresh() {

  },

  onReachBottom() {

  },

  onShareAppMessage() {

  }
})