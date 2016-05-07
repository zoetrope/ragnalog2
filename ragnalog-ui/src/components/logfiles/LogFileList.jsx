import React, {Component, PropTypes} from 'react';
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import * as theme from "../../RagnalogTheme";
import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
  TableFooter
} from "material-ui/Table";
import TextField from 'material-ui/TextField';
import Pagination from "./Pagination"

class LogFileList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedRows: "none"
    }
  }

  handleRowSelection = (selectedRows) => {

    this.setState({
      selectedRows: selectedRows
    });
    console.log("handleRowSelection", selectedRows);
  };

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

    return <Table selectable={true} multiSelectable={true} enableSelectAll={true}
                  onRowSelection={this.handleRowSelection}>
      <TableHeader displaySelectAll={true} adjustForCheckbox={true}>
        <TableRow onChange={e=>e.stopPropagation()}>
          <TableHeaderColumn>Archive Name</TableHeaderColumn>
          <TableHeaderColumn>Log Name</TableHeaderColumn>
          <TableHeaderColumn>Log Type</TableHeaderColumn>
          <TableHeaderColumn>Extra</TableHeaderColumn>
          <TableHeaderColumn style={{width:40}}>Menu</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={true} showRowHover={true} deselectOnClickaway={false}>
        {this.props.logFiles
          .map(logFile => {
            return <TableRow key={logFile.id} selectable={true} onChange={e=>e.stopPropagation()}>
              <TableRowColumn>{logFile.archiveName}</TableRowColumn>
              <TableRowColumn>{logFile.logName}</TableRowColumn>
              <TableRowColumn>{logFile.logType}</TableRowColumn>
              <TableRowColumn>
                <TextField name={logFile.id}
                           value={logFile.extra}
                           onClick={e=>e.stopPropagation()}/>
              </TableRowColumn>
              <TableRowColumn style={{width:40}}>{rightIconMenu(logFile)}</TableRowColumn>
            </TableRow>
          })}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableRowColumn colSpan="5">
            <Pagination page={this.props.page} limit={this.props.limit} onPageChange={this.props.onPageChange} />
          </TableRowColumn>
        </TableRow>
      </TableFooter>
    </Table>
  }
}

export default LogFileList;
