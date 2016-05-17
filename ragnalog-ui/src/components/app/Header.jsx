import React, {PropTypes, Component} from "react";
import {AppBar, Styles} from "material-ui";
import Drawer from "material-ui/Drawer";
import MenuItem from "material-ui/MenuItem";
import {Link} from 'react-router'
import {ToolbarSeparator} from 'material-ui/Toolbar';
import * as theme from "../../RagnalogTheme";

const styles = {
  separator: {
    backgroundColor: theme.palette.primary2Color
  },
  subtitle: {
    marginLeft: "25px",
    fontSize: "80%"
  }
};
class Header extends Component {

  constructor(props) {
    super(props);
    this.state = {open: false};
  }

  handleToggle = () => {
    this.setState({open: !this.state.open});
  };

  render() {

    const title = <span>Ragnalog<ToolbarSeparator style={styles.separator}/><span
      style={styles.subtitle}>{this.props.title}</span></span>;
    const appBar = <AppBar title={title} onLeftIconButtonTouchTap={this.handleToggle}/>;

    return (
      <header className="header">
        {appBar}
        <Drawer open={this.state.open}>
          {appBar}
          <MenuItem>Container</MenuItem>
          <MenuItem><Link to="/">Home</Link></MenuItem>
          <MenuItem><Link to="/containers/default">default</Link></MenuItem>
          <MenuItem><Link
            to="/containers/default/logfiles?archiveId=test&status=unregistered">logfiles</Link></MenuItem>
          <MenuItem>Admin</MenuItem>
          <MenuItem><Link to="/containers">containers</Link></MenuItem>

        </Drawer>
      </header>
    );
  }
}

export default Header;
