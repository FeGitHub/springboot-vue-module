import Vue from "vue";
import App from "./App";
import router from "./router";
import "iview/dist/styles/iview.css";
import iView from "view-design";
import "babel-polyfill";
import "./styles/index.scss";
import axios from "axios";
import store from './store'
import QS from "qs";
Vue.prototype.$axios = axios;
Vue.config.productionTip = false;
Vue.prototype.qs = QS;
Vue.use(iView);
new Vue({
  el: "#app",
  router,
  store,
  render: h => h(App)
});
