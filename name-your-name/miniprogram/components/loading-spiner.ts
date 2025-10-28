Component({
  data: {
    texts: [
      "正在综合分析取名要求",
      "请稍等片刻...... ",
      "正在为您筛选最合适的英文名",
      "💡更多中文名谐音建议设置【名字首字母开头】"
    ],
    currentTextIndex: 0,
    intervalId: -1
  },

  methods: {
    switchText() {
      let nextIndex = (this.data.currentTextIndex + 1) % this.data.texts.length;
      this.setData({ currentTextIndex: nextIndex });
    },

    startLoading() {
      const boundSwitchText = this.switchText.bind(this);
      this.stopLoading();
      let id = setInterval(boundSwitchText, 1000);
      this.setData({ intervalId: id });
    },

    stopLoading() {
      if (this.data.intervalId) {
        clearInterval(this.data.intervalId);
        this.setData({
          intervalId: -1
        });
      }
    }
  },

  attached() {
    this.startLoading();
  },

  detached() {
    this.stopLoading();
  }
});