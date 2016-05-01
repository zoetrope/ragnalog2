import React, {Component, PropTypes} from "react";
import {Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn} from "material-ui/Table";
import RaisedButton from "material-ui/RaisedButton";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";
import IconMenu from "material-ui/IconMenu";
import MenuItem from "material-ui/MenuItem";
import MoreVertIcon from "material-ui/svg-icons/navigation/more-vert";
import IconButton from "material-ui/IconButton";
import ContainerModalDialog from "./ContainerModalDialog";
import * as Actions from "../../actions/ContainerAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as theme from "../../RagnalogTheme";
import Snackbar from 'material-ui/Snackbar';

const style = {
  margin: 12
};

class Containers extends Component {

  static propTypes = {
    isFetching: PropTypes.bool.isRequired,
    error: PropTypes.bool.isRequired,
    errorMessage: PropTypes.node.isRequired,
    containers: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
        description: PropTypes.string,
        status: PropTypes.string.isRequired
      })
    ),

    fetchContainers: PropTypes.func.isRequired,
    addContainer: PropTypes.func.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      openMessage: false,
      openDialog: false
    };
  }

  componentWillMount() {
    this.props.fetchContainers();
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      openMessage: nextProps.error
    });
  }

  handleOpenDialog = () => {
    this.setState({openDialog: true});
  };

  handleCloseDialog = () => {
    this.setState({openDialog: false});
  };

  handleCloseMessage = () => {
    this.setState({
      openMessage: false
    });
  };

  render() {

    const rightIconMenu = (
      <IconMenu iconButtonElement={
          <IconButton>
            <MoreVertIcon color={theme.palette.accent1Color} />
          </IconButton>
        }
      >
        <MenuItem primaryText="View"/>
        <MenuItem primaryText="Activate"/>
        <MenuItem primaryText="Edit"/>
        <MenuItem primaryText="Delete"/>
      </IconMenu>
    );

    return <div>
      <h2>Containers</h2>
      <TextField hintText="Search"/>
      <RaisedButton
        label="Add Container" style={style}
        icon={<FontIcon className="material-icons">add_circle</FontIcon>}
        onTouchTap={this.handleOpenDialog}
      />
      <ContainerModalDialog
        open={this.state.openDialog}
        onSubmit={this.props.addContainer}
      />
      <Table selectable={false}>
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
              <TableRowColumn>{rightIconMenu}</TableRowColumn>
            </TableRow>
          })}
        </TableBody>
      </Table>
      <Snackbar
        open={this.state.openMessage}
        message={this.props.errorMessage}
        autoHideDuration={6000}
        onRequestClose={this.handleCloseMessage}
      />
    </div>
  }
}

function mapStateToProps(state) {
  console.log(state.ContainerReducer);
  return {
    isFetching: state.ContainerReducer.isFetching,
    error: state.ContainerReducer.error,
    errorMessage: state.ContainerReducer.errorMessage,
    containers: state.ContainerReducer.containers
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(Containers);
