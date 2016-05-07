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
import RaisedButton from "material-ui/RaisedButton";
import FlatButton from "material-ui/FlatButton";
import FontIcon from "material-ui/FontIcon";
import Dialog from "material-ui/Dialog";

const styles = {
  button: {
    margin: 12
  },
  rightButton: {
    margin: 12,
    float: 'right'
  },
  buttonGroup: {
    margin: "0 20px"
  }
};

class LogFileList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedRows: "none",
      openBulkSetDialog: false,
      filterValue: "",
      logType: "",
      extra: ""
    }
  }

  handleRowSelection = (selectedRows) => {
    //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/3734
    // I can not call setState in onRowSelection handler.
    // this.setState({
    //   selectedRows: selectedRows,
    // });
    this.state.selectedRows = selectedRows;
  };

  shouldComponentUpdate(nextProps, nextState) {
    //TODO: reconstruct the component tree.
    // When the TextFile value is changed, the rendering speed of the table is too slow.
    return this.state.filterValue === nextState.filterValue
      && this.state.logType === nextState.logType
      && this.state.extra === nextState.extra;
  }

  handleOpenBulkSetDialog = () => {
    this.setState({openBulkSetDialog: true})
  };
  handleCloseBulkSetDialog = () => {
    this.setState({openBulkSetDialog: false})
  };

  handleFilterValueChange = (e) => {
    this.setState({filterValue: e.target.value});
    e.stopPropagation();
  };
  handleLogTypeChange = (e) => {
    this.setState({logType: e.target.value});
    e.stopPropagation();
  };
  handleExtraChange = (e) => {
    this.setState({extra: e.target.value});
    e.stopPropagation();
  };

  render() {

    //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/2189
    // We should call event.stopPropagation on TableRow, if we use Tab Component.
    return <div>
      <div style={styles.buttonGroup}>
        <TextField
          hintText="filter"
          // value={this.state.filterValue}
          onChange={this.handleFilterValueChange}
        />
        <FlatButton
          label="Filter" style={styles.button}
          onTouchTap={e => this.props.onApplyFilter(e, this.state.filterValue)}
        />
        <RaisedButton
          label="Register" style={styles.rightButton}
          primary={true}
          icon={<FontIcon className="material-icons">add_circle</FontIcon>}
          onTouchTap={this.props.onRegister}
        />
        <RaisedButton
          label="Bulk set" style={styles.rightButton}
          secondry={true}
          icon={<FontIcon className="material-icons">edit</FontIcon>}
          onTouchTap={this.handleOpenBulkSetDialog}
        />
        <Dialog
          title="Bulk set to selected rows"
          modal={false}
          open={this.state.openBulkSetDialog}
          onRequestClose={this.handleCloseBulkSetDialog}
        >
          <TextField
            hintText="log type"
            onChange={this.handleLogTypeChange}
          />
          <FlatButton
            label="set"
            onTouchTap={e => this.props.onSetLogType}
            onTouchTap={e => this.props.onSetLogType(e, this.state.selectedRows, this.state.logType)}
          />
          <br/>
          <TextField
            hintText="extra"
            onChange={this.handleExtraChange}
          />
          <FlatButton
            label="set"
            onTouchTap={e => this.props.onSetExtra(e, this.state.selectedRows, this.state.extra)}
          />
        </Dialog>
      </div>
      <Table selectable={true} multiSelectable={true} enableSelectAll={true}
             onRowSelection={this.handleRowSelection}>
        <TableHeader displaySelectAll={true} adjustForCheckbox={true}>
          <TableRow onChange={e=>e.stopPropagation()}>
            <TableHeaderColumn>Archive Name</TableHeaderColumn>
            <TableHeaderColumn>Log Name</TableHeaderColumn>
            <TableHeaderColumn>Log Type</TableHeaderColumn>
            <TableHeaderColumn>Extra</TableHeaderColumn>
          </TableRow>
        </TableHeader>
        <TableBody displayRowCheckbox={true} showRowHover={true} deselectOnClickaway={false} preScanRows={false}>
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
    </div>
  }
}

export default LogFileList;
