Page({
  data: {
    answer: {},
    answerInString: "",
    name: "",
    filteredNames:[],
    allNames:[],
  },

  onNameChange(e: any) {
    const name = e.detail.value;
    this.setData({"name": name,})
  },
  sendRequest() {
    const app = getApp();
    let keyword = this.data.name.trimEnd();
    const filteredNames = this.data.allNames.filter((item:any) => {
      return item.name.startsWith(keyword)}
    );
    let names = "";
    filteredNames.forEach((item: any) => {
      names = names + "- **" + item.name + "**: " + item.meaning + "\n";
    });
    const obj = app.towxml(names, 'markdown', {theme: 'light'});
    this.setData({"answer": obj});
  },

  onLoad() {
    const app = getApp();
    let names: string = "";
    this.setData({"allNames": app.globalData.dictNames});
    this.data.allNames.forEach((item: any) => {
      names = names + "- **" + item.name + "**: " + item.meaning + "\n";
    });
    const obj = app.towxml(
      names, 'markdown', {theme: 'light'}
    );
    this.setData({"answer": obj});
  },

  onReady() {},
  onShow() {},
  onHide() {},
  onUnload() {},
  onPullDownRefresh() {},
  onReachBottom() {},
  onShareAppMessage() {}
})