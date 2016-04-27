import React from "react";
import ReactDOM from "react-dom";
import injectTapEventPlugin from "react-tap-event-plugin";
import {Provider} from "react-redux";
import {Router, Route, IndexRoute, browserHistory} from "react-router";
import {syncHistoryWithStore} from "react-router-redux";
import App from "./components/app/App";
import Container from "./components/container/Container";
import Containers from "./components/containers/Containers";
import configureStore from "./store/configureStore";
import ReconnectingWebSocket from "reconnectingwebsocket"

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
        <IndexRoute component={Containers}/>
        <Route path="/containers" component={Containers}/>
        <Route path="/container/:id" component={Container}/>
      </Route>
    </Router>
  </Provider>,
  document.getElementById("root")
);

const ws = new ReconnectingWebSocket('ws://localhost:8686/socket');

ws.onopen = function () {
  console.log("onopen")
  ws.send("fffffffffffff")
};

ws.onerror = function (error) {
  console.log('WebSocket Error ' + error);
};

ws.onmessage = function (e) {
  console.log('Server: ' + e.data);
};
