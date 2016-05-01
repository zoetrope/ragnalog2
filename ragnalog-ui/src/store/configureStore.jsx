import {compose, applyMiddleware, createStore, combineReducers} from "redux";
import {routerReducer, routerMiddleware, push} from "react-router-redux";
import thunkMiddlware from "redux-thunk";
import rootReducer from "../reducers";

export default function configureStore(history, initialState) {

  const store = createStore(
    rootReducer,
    initialState,
    compose(
      applyMiddleware(thunkMiddlware, routerMiddleware(history)),
      window.devToolsExtension ? window.devToolsExtension() : f =>f
    )
  );

  if (module.hot) {
    // Enable Webpack hot module replacement for reducers
    module.hot.accept('../reducers', () => {
      const nextReducer = require('../reducers');
      store.replaceReducer(nextReducer);
    });
  }

  return store;
}
