import Vue from 'vue';
import Router from 'vue-router';
import person from '@/pages/PERSON';
import test from '@/pages/test';
import showPdfDemo from '@/pages/showPdfDemo';

Vue.use(Router)

/****
 *  路由控制器
 */
export default new Router({
  routes: [
    {
      path: '/',
      name: 'person',
      component: person
    },
    {
      path: '/test',
      name: 'test',
      component: test
    },
    {
      path: '/showPdfDemo',
      name: 'showPdfDemo',
      component: showPdfDemo
    }
  ]
})
