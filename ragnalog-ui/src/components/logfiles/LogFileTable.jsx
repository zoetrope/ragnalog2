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

class LogFileTable extends Component {

  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(nextProps, nextState) {
    return this.props.logFiles !== nextProps.logFiles
      || this.props.page !== nextProps.page
      || this.props.limit !== nextProps.limit;
  }

  render() {

    //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/2189
    // We should call event.stopPropagation on TableRow, if we use Tab Component.
    return <Table selectable={true} multiSelectable={true} onRowSelection={this.props.onRowSelection}>
      <TableHeader enableSelectAll={true} displaySelectAll={true} adjustForCheckbox={true}>
        <TableRow onChange={e=>e.stopPropagation()}>
          <TableHeaderColumn>Archive Name</TableHeaderColumn>
          <TableHeaderColumn>Log Name</TableHeaderColumn>
          <TableHeaderColumn>Log Type</TableHeaderColumn>
          <TableHeaderColumn>Extra</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={true} showRowHover={true} deselectOnClickaway={false}>
        {this.props.logFiles
          .map(logFile => {
            return <TableRow
              key={logFile.id}
              selectable={true}
              selected={logFile.selected}
              onChange={e=>e.stopPropagation()}
            >
              <TableRowColumn>{logFile.archiveName}</TableRowColumn>
              <TableRowColumn>{logFile.logName}</TableRowColumn>
              <TableRowColumn>{logFile.logType}</TableRowColumn>
              <TableRowColumn>
                <TextField name={logFile.id}
                           value={logFile.extra}
                           onClick={e=>e.stopPropagation()}/>
              </TableRowColumn>
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
