import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";

Vue.use(VueRouter)

const routes = [
  /*{
    path: '/',
    name: 'Manage',
    component: () => import('../views/Manage.vue'),
    redirect: "/home",
    children:[
      {
        path: 'home', name: 'Home', component: () => import('../views/Home.vue'),
      },
      {
        path: 'user', name: 'User', component: () => import('../views/User.vue'),
      },
      {
        path: 'person', name: 'Person', component: () => import('../views/Person'),
      },
        {
            path: 'file', name: 'File', component: () => import('../views/File'),
        },
      {
        path: 'role', name: 'Role', component: () => import('../views/Role'),
      },
      {
        path: 'menu', name: 'Menu', component: () => import('../views/Menu'),
      },
    ]
  },*/

  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register')
  },
  {
    path: '/map',
    name: 'Map',
    component: () => import('../views/Map')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404')
  },
  {
    path: '/front',
    name: 'Front',
    component: () => import('../views/front/Front'),
    children:[
      {
        path: 'home',
        name: 'FrontHome',
        component: () => import('../views/front/Home')
      },
      {
        path: 'person',
        name: 'FrontPerson',
        component: () => import('../views/front/Person')
      },
      {
        path: 'video',
        name: 'Video',
        component: () => import('../views/front/Video')
      },
      {
        path: 'videoDetail',
        name: 'VideoDetail',
        component: () => import('../views/front/VideoDetail')
      },
      {
        path: 'article',
        name: 'Article',
        component: () => import('../views/front/Article')
      },
      {
        path: 'articleDetail',
        name: 'ArticleDetail',
        component: () => import('../views/front/ArticleDetail')
      },
      {
        path: 'password',
        name: 'FrontPassword',
        component: () => import('../views/front/Password')
      },
    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

//注意刷新页面会重置路由
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if(storeMenus){

    //获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if(!currentRouteNames.includes('Manage')){

      //拼装动态路由
      const mangeRoutes = {path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home",children:[
          {path: 'person', name: 'Person', component:() => import('../views/Person')},
          {path: 'password', name: 'Password', component:() => import('../views/Password')},
        ]}
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if(item.path){
          //当且仅当path不为空的时候才设置路由
          let itemMenu = {path:item.path.replace("/", ""), name: item.name, component:() => import('../views/' + item.pagePath + '.vue')}
          mangeRoutes.children.push(itemMenu)
        }else if(item.children.length){
          item.children.forEach(item => {
            if(item.path){
              let itemMenu = {path:item.path.replace("/", ""), name: item.name, component:() => import('../views/' + item.pagePath + '.vue')}
              mangeRoutes.children.push(itemMenu)
            }
          })
        }
      })

      //动态添加到现在的路由对象中去
      router.addRoute(mangeRoutes)
    }


  }
}

setRoutes()

router.beforeEach((to, from, next) =>{
  localStorage.setItem("currentPathName", to.name)   // 设置当前路由的名称
  store.commit("setPath")
  // 未找到路由的情况
  if (!to.matched.length) {
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
      next("/404")
    } else {
      // 跳回登录页面
      next("/login")
    }
  }
  next()
})
export default router
