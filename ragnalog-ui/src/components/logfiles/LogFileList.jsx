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
import ChevronLeft from 'material-ui/svg-icons/navigation/chevron-left';
import ChevronRight from 'material-ui/svg-icons/navigation/chevron-right';
import TextField from 'material-ui/TextField';

const styles = {
  footerContent: {
    float: 'right'
  },
  footerText: {
    float: 'right',
    paddingTop: '16px',
    height: '16px'
  }
};

class LogFileList extends Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedRows: "none"
    }
  }

  handlePageClick = (page) => {
    console.log("handlePageClick", page)
  };

  handleRowSelection = (selectedRows) => {
    console.log("handleRowSelection", selectedRows);

    this.setState({
      selectedRows: selectedRows
    })
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

    const {page, limit} = this.props;
    const pagination = (<div style={styles.footerContent}>
      <IconButton disabled={page === 0} onClick={() => this.props.onChangePage(page - 1)}>
        <ChevronLeft/>
      </IconButton>
      <IconButton disabled={page >= limit} onClick={() => this.props.onChangePage(page + 1)}>
        <ChevronRight/>
      </IconButton>
      {page + ' of ' + limit}
    </div>);

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
            {pagination}
          </TableRowColumn>
        </TableRow>
      </TableFooter>
    </Table>
  }
}

export default LogFileList;
