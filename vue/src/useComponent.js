import SubmitElDialog from '@/components/SubmitElDialog';
import ShowPdf from '@/components/ShowPdf';
import ListTip from '@/components/ListTip';
// 当我们需要进行多个组件的注册时候, 假如说都写到main.js里面的话,
// 这样就会造成main.js文件的臃肿与杂乱
export default {
  install (Vue) {
    Vue.component('SubmitElDialog', SubmitElDialog)
    Vue.component('ShowPdf', ShowPdf)
    Vue.component('ListTip', ListTip)
  }
}
