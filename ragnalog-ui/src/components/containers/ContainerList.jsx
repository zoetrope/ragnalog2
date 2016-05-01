import React, {Component, PropTypes} from 'react';
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import * as theme from "../../RagnalogTheme";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";

class ContainerList extends Component {

  constructor(props) {
    super(props);
  }

  render() {

    const rightIconMenu = (container) => (
      <IconMenu iconButtonElement={
          <IconButton>
            <MoreVertIcon color={theme.palette.accent1Color} />
          </IconButton>
        }
      >
        <MenuItem primaryText="View" onTouchTap={() => this.props.onView(container)}/>
        <MenuItem primaryText="Activate" onTouchTap={() => this.props.onActivate(container)}/>
        <MenuItem primaryText="Edit" onTouchTap={() => this.props.onEdit(container)}/>
        <MenuItem primaryText="Delete" onTouchTap={() => this.props.onDelete(container)}/>
      </IconMenu>
    );

    return <Table selectable={false}>
      <TableHeader displaySelectAll={false}>
        <TableRow>
          <TableHeaderColumn>ID</TableHeaderColumn>
          <TableHeaderColumn>Name</TableHeaderColumn>
          <TableHeaderColumn>Description</TableHeaderColumn>
          <TableHeaderColumn></TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={false} showRowHover={true}>
        {this.props.containers.map(container => {
          return <TableRow key={container.id}>
            <TableRowColumn>{container.id}</TableRowColumn>
            <TableRowColumn>{container.name}</TableRowColumn>
            <TableRowColumn>{container.description}</TableRowColumn>
            <TableRowColumn>{rightIconMenu(container)}</TableRowColumn>
          </TableRow>
        })}
      </TableBody>
    </Table>
  }
}

export default ContainerList;
