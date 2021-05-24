let base_url='http://127.0.0.1:8100';//请求地址

function service(options = {}) {
    options.url = `${base_url}${options.url}`;
    //配置请求头
    options.header = {
        'content-type': 'application/x-www-form-urlencoded'
    }

    return new Promise((resolved, rejected) => {
        //成功
        options.success = (res) => {
            if (Number(res.data.code) == 0) {  //请求成功
                resolved(res.data.data);
            } else {
                uni.showToast({
                    icon: 'none',   
                    duration: 3000,
                    title: `${res.data.msg}`
                });
                rejected(res.data.msg);//错误
            }
        }
        //错误
        options.fail = (err) => {
            rejected(err); //错误
        }
        uni.request(options);
    });
}
export default service;
