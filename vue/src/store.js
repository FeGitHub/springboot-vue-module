import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);
const store = new Vuex.Store({
  state: {
    token: localStorage.getItem('token') ? localStorage.getItem('token') : ''
  },
  mutations: {
    setToken (state, token) {// this.$store.commit("setToken", "XXX");
      state.token = token;
      localStorage.setItem("token", token);     //存储token
    },
    delToken (state) {// this.$store.commit("delToken");
      state.token = '';
      localStorage.removeItem("token");    //删除token
    }
  }
});

export default store;
