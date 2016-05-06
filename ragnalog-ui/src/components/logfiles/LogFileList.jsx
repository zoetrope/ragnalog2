import React, {Component, PropTypes} from 'react';
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import * as theme from "../../RagnalogTheme";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

class LogFileList extends Component {

  constructor(props) {
    super(props);
  }

  render() {

    const rightIconMenu = (logFile) => (
      <IconMenu iconButtonElement={
          <IconButton>
            <MoreVertIcon color={theme.palette.accent1Color} />
          </IconButton>
        }
      >
        <MenuItem primaryText="Register" onTouchTap={() => this.props.onRegister(logFile)}/>
        <MenuItem primaryText="Unregister" onTouchTap={() => this.props.onUnregister(logFile)}/>
        <MenuItem primaryText="Cancel" onTouchTap={() => this.props.onCancel(logFile)}/>
      </IconMenu>
    );

    return <Table selectable={true}>
      <TableHeader displaySelectAll={false}>
        <TableRow>
          <TableHeaderColumn>Archive Name</TableHeaderColumn>
          <TableHeaderColumn>Log Name</TableHeaderColumn>
          <TableHeaderColumn>Log Type</TableHeaderColumn>
          <TableHeaderColumn>Extra</TableHeaderColumn>
          <TableHeaderColumn style={{width:40}}>Menu</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={false} showRowHover={true}>
        {this.props.logFiles
          .map(logFile => {
            return <TableRow key={logFile.id}>
              <TableRowColumn>{logFile.archiveName}</TableRowColumn>
              <TableRowColumn>{logFile.logName}</TableRowColumn>
              <TableRowColumn>{logFile.logType}</TableRowColumn>
              <TableRowColumn>{logFile.extra}</TableRowColumn>
              <TableRowColumn style={{width:40}}>{rightIconMenu(logFile)}</TableRowColumn>
            </TableRow>
          })}
      </TableBody>
    </Table>
  }
}

export default LogFileList;
