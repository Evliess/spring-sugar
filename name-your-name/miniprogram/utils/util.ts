interface RequestOptions {
  url: string,
  method: string,
  data?: any,
  header?: any
}
// export const BASE_URL = 'http://localhost:8080';
export const BASE_URL = 'https://www.jiangxinma.top';
export const request = <T = any>(options: RequestOptions) : Promise<T> => {
  return new Promise((resolve, reject)=> {
    wx.request({
      url: BASE_URL + options.url,
      method: options.method,
      data: options.data || {},
      header: options.header || {},
      timeout: 120000,
      success: (res) => {
        if(res.statusCode == 200) {
          resolve(res.data as T);
        } else {
          reject(res)
        }
      },
      fail: (err) => {reject(err);},
    });
  });
}

export const fetchDictNamesByType = (url: string, type: string, openId: string) => request<any>({url: url, method: 'POST', data: {"type": type, 'X-Openid': openId}});
export const fetchValidToken = (url: string, openId: string) => request<any>({url: url, method: 'POST', data: {"openId": openId}});
export const fetchSugar = (url: string, openId: string, token: string, data: any)=> request<any>({url: url, method: 'POST', data: data, 
                            header: {'content-type': 'application/json', 'X-token': token, 'X-openId': openId}});
export const fetchOpenid = (url: string, code: string) => request<any>({url: url, method: 'POST', data: {"code": code}});
export const checkToken = (url: string, openId: string, token: string) => request<any>({url: url, method: 'POST',
                            header: {'content-type': 'application/json', 'X-token': token, 'X-openId': openId}});
