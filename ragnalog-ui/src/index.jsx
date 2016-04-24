import React from "react";
import ReactDOM from "react-dom";
import injectTapEventPlugin from "react-tap-event-plugin";
import {Provider} from "react-redux";
import {Router, Route, IndexRoute, browserHistory} from "react-router";
import {syncHistoryWithStore} from "react-router-redux";
import App from "./containers/App";
import MainSection from "./components/MainSection";
import HelloWorld from "./components/HelloWorld";
import Container from "./components/Container";
import Containers from "./components/Containers";
import configureStore from "./store/configureStore";

//Needed for React Developer Tools
window.React = React;

//Needed for onTouchTap
//Can go away when react 1.0 release
//Check this repo:
//https://github.com/zilverline/react-tap-event-plugin
injectTapEventPlugin();

const store = configureStore();
const history = syncHistoryWithStore(browserHistory, store);

ReactDOM.render(
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={App}>
        <IndexRoute component={HelloWorld}/>
        <Route path="/todos" component={MainSection}/>
        <Route path="/hello" component={HelloWorld}/>
        <Route path="/containers" component={Containers}/>
        <Route path="/container/:id" component={Container}/>
      </Route>
    </Router>
  </Provider>,
  document.getElementById("root")
);
