var OVER_WRITE_PRINT = false;//是否重写打印方法
var SHOW_LOG = false;//是否打印日志信息
/****
 * 自定义的打印方法，实际上是告诉父页面此时子页面触发了点击的打印的动作
 */
function customPrint() {
    var data = {operatorType: "print",}
    window.parent.postMessage(data, '*');
}

/***
 * 监听父页面的消息
 */
window.addEventListener('message', (e) => {
    let data = e.data;
    if (SHOW_LOG) {
        console.log("来自父组件的信息：", data)
    }
    if (data.operatorType === 'print') {//打印
        window.print();
    }
})


/****
 * 解析url的参数
 * @param urlStr
 * @returns {Object}
 * @constructor
 */
function GetRequest(urlStr) {
    if (typeof urlStr == "undefined") {
        var url = decodeURI(location.search); //获取url中"?"符后的字符串
    } else {
        var url = "?" + urlStr.split("?")[1];
    }
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}
