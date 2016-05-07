import React, {Component, PropTypes} from 'react';
import TextField from 'material-ui/TextField';
import RaisedButton from "material-ui/RaisedButton";
import FlatButton from "material-ui/FlatButton";
import FontIcon from "material-ui/FontIcon";
import Dialog from "material-ui/Dialog";
import LogFileTable from "./LogFileTable";

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
      openBulkSetDialog: false,
      filterValue: "",
      logType: "",
      extra: "",
      selectedRows: "none"
    }
  }

  handleOpenBulkSetDialog = () => {
    this.setState({openBulkSetDialog: true});
  };
  handleCloseBulkSetDialog = () => {
    this.setState({openBulkSetDialog: false});
  };

  //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/2189
  // We should call event.stopPropagation on TextField.onChange, if we use Tab Component.
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
  
  handleRowSelection = (selectedRows) => {
    //CAUTION: material-ui's bug https://github.com/callemall/material-ui/issues/3734
    // When setState is called in onRowSelection handler, selected rows checkbox is cleared.
    this.state.selectedRows = selectedRows;
    // this.setState({
    //   selectedRows: selectedRows
    // });
  };

  render() {

    return <div>
      <div style={styles.buttonGroup}>
        <TextField
          hintText="filter"
          // value={this.state.filterValue}
          onChange={this.handleFilterValueChange}
        />
        <FlatButton
          label="Filter" style={styles.button}
          onTouchTap={e => this.props.onApplyFilter(this.state.filterValue)}
        />
        <RaisedButton
          label="Register" style={styles.rightButton}
          primary={true}
          icon={<FontIcon className="material-icons">add_circle</FontIcon>}
          onTouchTap={e => this.props.onRegister(this.state.selectedRows)}
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
            onTouchTap={e => this.props.onSetLogType(this.state.selectedRows, this.state.logType)}
          />
          <br/>
          <TextField
            hintText="extra"
            onChange={this.handleExtraChange}
          />
          <FlatButton
            label="set"
            onTouchTap={e => this.props.onSetExtra(this.state.selectedRows, this.state.extra)}
          />
        </Dialog>
      </div>
      <LogFileTable
        logFiles={this.props.logFiles}
        page={this.props.page}
        limit={this.props.limit}
        onPageChange={this.props.onPageChange}
        onRowSelection={this.handleRowSelection}
        onExtraChange={(extra, index) => this.props.onSetExtra([index], extra)}
      />
    </div>
  }
}

export default LogFileList;
