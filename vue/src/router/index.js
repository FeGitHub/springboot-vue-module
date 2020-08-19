
import Vue from "vue";
import Router from "vue-router";
import PERSON from "@/components/PERSON";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      name: "PERSON",
      component: PERSON
    }
  ]
});
