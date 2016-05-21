import React, {PropTypes, Component} from "react";
import {AppBar, Styles} from "material-ui";
import Drawer from "material-ui/Drawer";
import MenuItem from "material-ui/MenuItem";
import {Link} from 'react-router'
import {ToolbarSeparator} from 'material-ui/Toolbar';
import * as theme from "../../RagnalogTheme";
import Badge from 'material-ui/Badge';
import FontIcon from "material-ui/FontIcon";
import IconMenu from 'material-ui/IconMenu';

const styles = {
  separator: {
    backgroundColor: theme.palette.primary2Color
  },
  subtitle: {
    marginLeft: "25px",
    fontSize: "80%"
  },
  badge: {
    padding: "12px 18px 12px 12px"
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
    const messages = this.props.messages;
    const unreadNum = messages.filter(m => m.unread).length;

    const icon = unreadNum === 0 ?
      <FontIcon style={styles.badge} className="material-icons" color={theme.palette.accent2Color}>notifications</FontIcon> :
      <Badge
        badgeContent={unreadNum}
        primary={true}
        style={styles.badge}
      >
        <FontIcon className="material-icons" color={theme.palette.accent2Color}>notifications</FontIcon>
      </Badge>;

    const notification = (<div>
      <IconMenu
        iconButtonElement={icon}
        targetOrigin={{horizontal: 'right', vertical: 'top'}}
        anchorOrigin={{horizontal: 'right', vertical: 'top'}}
      >
        {messages.map(m =>
          <MenuItem
            focusState="none"
            key={m.id}
            primaryText={m.message}
            secondaryText={m.date.toISOString()}
          /> )}
      </IconMenu>
    </div>);

    const title = <span>Ragnalog<ToolbarSeparator style={styles.separator}/><span
      style={styles.subtitle}>{this.props.title}</span></span>;
    const appBar = <AppBar
      title={title}
      onLeftIconButtonTouchTap={this.handleToggle}
      iconElementRight={notification}
    />;

    return (
      <header className="header">
        {appBar}
        <Drawer
          open={this.state.open}
          onRequestChange={(open) => this.setState({open})}
          docked={false}
        >
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
