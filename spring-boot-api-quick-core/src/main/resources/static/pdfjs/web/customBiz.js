function customPrint() {
    var data = {operatorType: "print",}
    window.parent.postMessage(data, '*');
}

window.addEventListener('message', (e) => {
    let data = e.data;
    console.log("父组件的信息：", data)
    if (data.operatorType === 'print') {//打印
        window.print();
    }
})
