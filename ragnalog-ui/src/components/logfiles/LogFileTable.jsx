import React, {Component, PropTypes} from 'react';
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
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import IconMenu from "material-ui/IconMenu";
import IconButton from "material-ui/IconButton";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import * as theme from "../../RagnalogTheme";

class LogFileTable extends Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(nextProps, nextState) {
    const update = this.props.logFiles !== nextProps.logFiles
      || this.props.page !== nextProps.page
      || this.props.limit !== nextProps.limit;
    // if (update) {
    //   console.log("should update: ", this.props.logFiles, nextProps.logFiles)
    // }
    return update;
  }

  render() {
    const rightIconMenu = (logFile) => (
      <IconMenu iconButtonElement={
        <IconButton>
          <MoreVertIcon color={theme.palette.accent1Color} />
        </IconButton>
      }
      >
        <MenuItem primaryText="Preview" onTouchTap={() => this.props.onPreview(logFile)}/>
      </IconMenu>
    );

    //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/2189
    // We should call event.stopPropagation on TableRow, if we use Tab Component.
    return <Table selectable={true} multiSelectable={true} onRowSelection={this.props.onRowSelection}>
      <TableHeader enableSelectAll={true} displaySelectAll={true} adjustForCheckbox={true}>
        <TableRow onChange={e=>e.stopPropagation()}>
          <TableHeaderColumn>Log Name</TableHeaderColumn>
          <TableHeaderColumn style={{width:180}}>Log Type</TableHeaderColumn>
          <TableHeaderColumn style={{width:180}}>Extra</TableHeaderColumn>
          <TableHeaderColumn style={{width:40}}>Menu</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={true} showRowHover={true} deselectOnClickaway={false}>
        {this.props.logFiles
          .map((logFile, index) => {
            return <TableRow
              key={logFile.id}
              selectable={true}
              selected={logFile.selected}
              onChange={e=>e.stopPropagation()}
            >
              <TableRowColumn>{logFile.archiveName}/{logFile.logName}</TableRowColumn>
              <TableRowColumn style={{width:180}}>
                <SelectField
                  value={logFile.logType}
                  onChange={(e, i, v) => this.props.onLogTypeChange(index, v)}
                >
                  {this.props.logTypes.map(logType => {
                    return (<MenuItem key={logType.id} value={logType.id} primaryText={logType.name}/>)
                  })}
                </SelectField>
              </TableRowColumn>
              <TableRowColumn style={{width:180}}>
                <TextField name={logFile.id}
                           value={logFile.extra}
                           onChange={e => this.props.onExtraChange(index, e.target.value)}
                           onClick={e=>e.stopPropagation()}/>
              </TableRowColumn>
              <TableRowColumn style={{width:40}}>{rightIconMenu(logFile)}</TableRowColumn>
            </TableRow>
          })}
      </TableBody>
      <TableFooter>
        <TableRow>
          <TableRowColumn colSpan="4">
            <Pagination page={this.props.page} limit={this.props.limit} onPageChange={this.props.onPageChange}/>
          </TableRowColumn>
        </TableRow>
      </TableFooter>
    </Table>
  }
}

export default LogFileTable;
