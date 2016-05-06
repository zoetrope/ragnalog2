import React, {PropTypes, Component} from "react";
import {AppBar, Styles} from "material-ui";
import Drawer from "material-ui/Drawer";
import MenuItem from "material-ui/MenuItem";
import {Link} from 'react-router'

class Header extends Component {

  constructor(props) {
    super(props);
    this.state = {open: false};
  }

  handleToggle = () => {
    this.setState({open: !this.state.open});
  };

  render() {
    
    const appBar = <AppBar title="Ragnalog" onLeftIconButtonTouchTap={this.handleToggle}/>;

    return (
      <header className="header">
        {appBar}
        <Drawer open={this.state.open}>
          {appBar}
          <MenuItem>Container</MenuItem>
          <MenuItem><Link to="/">Home</Link></MenuItem>
          <MenuItem><Link to="/containers/default">default</Link></MenuItem>
          <MenuItem><Link to="/containers/default/logfiles?archiveId=test&status=unregistered">logfiles</Link></MenuItem>
          <MenuItem>Admin</MenuItem>
          <MenuItem><Link to="/containers">containers</Link></MenuItem>
        </Drawer>
      </header>
    );
  }
}

export default Header;
