import React, {Component, PropTypes} from "react";
import Header from "./Header";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import * as theme from "../../RagnalogTheme";
import * as Actions from "../../actions/AppAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";

const muiTheme = getMuiTheme(theme);

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
  }

  componentWillMount() {
    this.props.fetchLogTypes();
  }

  render() {
    const {children} = this.props;
    return (
      <MuiThemeProvider muiTheme={muiTheme}>
        <div style={styles.content}>
          <Header title={this.props.title}/>
          <div style={styles.children}>{children}</div>
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

