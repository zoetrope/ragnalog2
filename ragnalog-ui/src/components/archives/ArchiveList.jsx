import React, {Component, PropTypes} from 'react';
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import * as theme from "../../RagnalogTheme";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import FileSizeFilter from "../../filters/FileSizeFilter"

class ArchiveList extends Component {

  constructor(props) {
    super(props);
  }

  render() {

    const rightIconMenu = (archive) => (
      <IconMenu iconButtonElement={
          <IconButton>
            <MoreVertIcon color={theme.palette.accent1Color} />
          </IconButton>
        }
      >
        <MenuItem primaryText="View" onTouchTap={() => this.props.onView(archive)}/>
        <MenuItem primaryText="Delete" onTouchTap={() => this.props.onDelete(archive)}/>
      </IconMenu>
    );

    return <Table selectable={false}>
      <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
        <TableRow>
          <TableHeaderColumn>File Name</TableHeaderColumn>
          <TableHeaderColumn>Size</TableHeaderColumn>
          <TableHeaderColumn>Uploaded</TableHeaderColumn>
          <TableHeaderColumn>Modified</TableHeaderColumn>
          <TableHeaderColumn style={{width:40}}>Menu</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={false} showRowHover={true}>
        {this.props.archives
          .map(archive => {
            return <TableRow key={archive.id}>
              <TableRowColumn>{archive.fileName}</TableRowColumn>
              <TableRowColumn>{FileSizeFilter(archive.size, 1)}</TableRowColumn>
              <TableRowColumn>{archive.uploadedDate}</TableRowColumn>
              <TableRowColumn>{archive.modifiedDate}</TableRowColumn>
              <TableRowColumn style={{width:40}}>{rightIconMenu(archive)}</TableRowColumn>
            </TableRow>
          })}
      </TableBody>
    </Table>
  }
}

export default ArchiveList;
