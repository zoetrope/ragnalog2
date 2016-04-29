import React, {Component, PropTypes} from "react";
import Header from "./Header";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import * as theme from "../../RagnalogTheme";

const muiTheme = getMuiTheme(theme);

export default class App extends Component {
  render() {
    const {children} = this.props;
    return (
      <MuiThemeProvider muiTheme={muiTheme}>
        <div>
          <Header/>
          <div>{children}</div>
        </div>
      </MuiThemeProvider>
    );
  }
}
