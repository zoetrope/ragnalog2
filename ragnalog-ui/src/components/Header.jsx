import React, {PropTypes, Component} from "react";
import {AppBar, Styles} from "material-ui";
import Drawer from "material-ui/Drawer";
import MenuItem from "material-ui/MenuItem";

class Header extends Component {

  constructor(props) {
    super(props);
    this.state = {open: false};
  }

  handleToggle = () => this.setState({open: !this.state.open});

  render() {
    return (
      <header className="header">
        <AppBar title="Ragnalog" onLeftIconButtonTouchTap={this.handleToggle}/>
        <Drawer open={this.state.open}>
          <AppBar title="Ragnalog" onLeftIconButtonTouchTap={this.handleToggle}/>
          <MenuItem>Menu Item</MenuItem>
          <MenuItem>Menu Item 2</MenuItem>
        </Drawer>
      </header>
    );
  }
}


export default Header;
