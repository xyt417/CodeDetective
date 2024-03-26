import { createStore } from 'vuex'
import UserModule from './user'
import ResultModule from './result'

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: UserModule,
    result: ResultModule,
  }
})
