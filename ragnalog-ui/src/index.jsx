import React from "react";
import ReactDOM from "react-dom";
import injectTapEventPlugin from "react-tap-event-plugin";
import {Provider} from "react-redux";
import {Router, Route, IndexRoute, browserHistory} from "react-router";
import {syncHistoryWithStore} from "react-router-redux";
import App from "./components/app/App";
import ContainerMain from "./components/containers/ContainerMain";
import ArchiveMain from "./components/archives/ArchiveMain";
import LogFileMain from "./components/logfiles/LogFileMain";
import configureStore from "./store/configureStore";
import ReconnectingWebSocket from "reconnectingwebsocket"
import * as Config from "./store/Configuration";

//Needed for React Developer Tools
window.React = React;

// Performance Tools
import Perf from 'react-addons-perf'
window.Perf = Perf;

//Needed for onTouchTap
//Can go away when react 1.0 release
//Check this repo:
//https://github.com/zilverline/react-tap-event-plugin
injectTapEventPlugin();

const store = configureStore(browserHistory);
const history = syncHistoryWithStore(browserHistory, store);

ReactDOM.render(
  <Provider store={store}>
    <Router history={history}>
      <Route path="/" component={App}>
        <IndexRoute component={ArchiveMain}/>
        <Route path="/containers" component={ContainerMain}/>
        <Route path="/containers/:containerId" component={ArchiveMain}/>
        <Route path="/containers/:containerId/logfiles" component={LogFileMain}/>
      </Route>
    </Router>
  </Provider>,
  document.getElementById("root")
);

// const ws = new ReconnectingWebSocket(Config.socketHost + '/socket');
//
// ws.onopen = function () {
//   console.log("onopen")
//   ws.send("fffffffffffff")
// };
//
// ws.onerror = function (error) {
//   console.log('WebSocket Error ' + error);
// };
//
// ws.onmessage = function (e) {
//   console.log('Server: ' + e.data);
// };
