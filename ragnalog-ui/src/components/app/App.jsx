import React, {Component, PropTypes} from "react";
import Header from "./Header";
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import * as RagnalogTheme from "./RagnalogTheme";

const muiTheme = getMuiTheme(RagnalogTheme);

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
