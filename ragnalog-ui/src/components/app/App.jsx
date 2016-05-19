import React, {Component, PropTypes} from "react";
import Header from "./Header";
import getMuiTheme from "material-ui/styles/getMuiTheme";
import MuiThemeProvider from "material-ui/styles/MuiThemeProvider";
import * as theme from "../../RagnalogTheme";
import * as Actions from "../../actions/AppAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {IntlProvider, addLocaleData} from "react-intl";
import enMessages from "../../i18n/messages.en-US.json";
import jaMessages from "../../i18n/messages.ja-JP.json";
import localeData from "../../i18n/localeData";

const muiTheme = getMuiTheme(theme);

const messages = {
  "en": enMessages,
  "ja": jaMessages
};

const styles = {
  content: {
    height: "100%"
  },
  children: {
    background: "#f0f0f0",
    height: document.body.clientHeight - 64
  }
};

class App extends Component {


  constructor(props) {
    super(props);
    this.language = "ja";
  }

  componentWillMount() {
    this.props.fetchLogTypes();
    addLocaleData(localeData(this.language));
    this.props.addMessage("test1");
    this.props.addMessage("test2");
    this.props.addMessage("test3");
   }

  render() {
    const {children} = this.props;
    return (
      <MuiThemeProvider muiTheme={muiTheme}>
        <IntlProvider locale={this.language} messages={messages[this.language]}>
          <div style={styles.content}>
            <Header 
              title={this.props.title}
              messages={this.props.messages}
              onMessageRead={this.props.readMessage}
            />
            <div style={styles.children}>{children}</div>
          </div>
        </IntlProvider>
      </MuiThemeProvider>
    );
  }
}

function mapStateToProps(state) {
  return {
    title: state.AppReducer.title,
    messages: state.AppReducer.messages
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

