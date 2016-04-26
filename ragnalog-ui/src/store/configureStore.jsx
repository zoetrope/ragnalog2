import {compose, applyMiddleware, createStore, combineReducers} from "redux";
import {routerReducer} from "react-router-redux";
import thunkMiddlware from "redux-thunk"
import rootReducer from "../reducers"

export default function configureStore(initialState) {

  const store = createStore(
    rootReducer,
    initialState,
    window.devToolsExtension ? compose(applyMiddleware(thunkMiddlware), window.devToolsExtension()) : applyMiddleware(thunkMiddlware)
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
