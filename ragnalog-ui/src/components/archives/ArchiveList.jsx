import React, {Component, PropTypes} from 'react';
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import * as theme from "../../RagnalogTheme";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import FileSizeFilter from "../../filters/FileSizeFilter";
import DateFormatFilter from "../../filters/DateFormatFilter";

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
          <TableHeaderColumn style={{width:70}}>Size</TableHeaderColumn>
          <TableHeaderColumn style={{width:130}}>Uploaded</TableHeaderColumn>
          <TableHeaderColumn style={{width:130}}>Modified</TableHeaderColumn>
          <TableHeaderColumn style={{width:40}}>Menu</TableHeaderColumn>
        </TableRow>
      </TableHeader>
      <TableBody displayRowCheckbox={false} showRowHover={true}>
        {this.props.archives
          .map(archive => {
            return <TableRow key={archive.id}>
              <TableRowColumn>{archive.fileName}</TableRowColumn>
              <TableRowColumn style={{width:70}}>{FileSizeFilter(archive.size, 1)}</TableRowColumn>
              <TableRowColumn style={{width:130}}>{DateFormatFilter(archive.uploadedDate, "YYYY/MM/DD HH:mm:ss")}</TableRowColumn>
              <TableRowColumn style={{width:130}}>{DateFormatFilter(archive.modifiedDate, "YYYY/MM/DD HH:mm:ss")}</TableRowColumn>
              <TableRowColumn style={{width:40}}>{rightIconMenu(archive)}</TableRowColumn>
            </TableRow>
          })}
      </TableBody>
    </Table>
  }
}

export default ArchiveList;
