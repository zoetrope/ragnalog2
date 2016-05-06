import React, {Component, PropTypes} from "react";
import Header from "./Header";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import * as theme from "../../RagnalogTheme";
import * as Actions from "../../actions/AppAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";

const muiTheme = getMuiTheme(theme);

class App extends Component {
  render() {
    const {children} = this.props;
    return (
      <MuiThemeProvider muiTheme={muiTheme}>
        <div>
          <Header title={this.props.title}/>
          <div>{children}</div>
        </div>
      </MuiThemeProvider>
    );
  }
}

function mapStateToProps(state) {
  return {
    title: state.AppReducer.title
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

