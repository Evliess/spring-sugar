// 小程序页面代码
createPayment() {
  const that = this;

  // 1. 先获取用户的 openid（通过 wx.login + 后端接口）
  const openid = this.data.openid;
  const amount = 100;  // 单位：分，这里表示1元

  // 2. 请求后端创建订单
  wx.request({
    url: 'https://your-domain.com/api/wxpay/create-order',
    method: 'POST',
    data: {
      openid: openid,
      amount: amount
    },
    success(res) {
      const payData = res.data;

      // 3. 直接用后端返回的参数调起支付
      //    不需要前端再做任何签名处理
      wx.requestPayment({
        timeStamp: payData.timeStamp,
        nonceStr: payData.nonceStr,
        package: payData.package,
        signType: payData.signType,
        paySign: payData.paySign,
        success() {
          console.log('支付成功');
          // 等待异步通知处理结果
        },
        fail(err) {
          console.log('支付失败', err);
        }
      });
    }
  });
}