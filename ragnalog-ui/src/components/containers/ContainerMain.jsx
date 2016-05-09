import React, {Component, PropTypes} from "react";
import RaisedButton from "material-ui/RaisedButton";
import TextField from "material-ui/TextField";
import FontIcon from "material-ui/FontIcon";
import ContainerModalDialog from "./ContainerModalDialog";
import * as Actions from "../../actions/ContainerAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import Snackbar from "material-ui/Snackbar";
import {Tabs, Tab} from "material-ui/Tabs";
import ContainerList from "./ContainerList";

const style = {
  margin: 12
};

class ContainerMain extends Component {

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
    addContainer: PropTypes.func.isRequired,
    updateContainer: PropTypes.func.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      openMessage: false,
      openDialog: false,
      create: false,
      target: null
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
    this.setState({
      openDialog: true,
      create: true,
      target: null
    });
  };

  handleCloseDialog = () => {
    this.setState({openDialog: false});
  };

  handleCloseMessage = () => {
    this.setState({
      openMessage: false
    });
  };

  handleViewContainer = (container)=> {
    this.props.navigateToViewContainer(container.id)
  };

  handleActivateContainer = (container)=> {
  };

  handleDeactivateContainer = (container) => {
  };

  handleEditContainer = (container)=> {
    this.setState({
      openDialog: true,
      create: false,
      target: container
    });
  };

  handleDeleteContainer = (container)=> {
  };

  render() {

    return <div>
      <TextField hintText="Filter"/>
      <RaisedButton
        label="Add Container" style={style}
        icon={<FontIcon className="material-icons">add_circle</FontIcon>}
        onTouchTap={this.handleOpenDialog}
      />
      <ContainerModalDialog
        open={this.state.openDialog}
        create={this.state.create}
        target={this.state.target}
        onSubmit={(id, name, desc) => this.state.create ? this.props.addContainer(id, name, desc) : this.props.updateContainer(id, name, desc)}
      />
      <Tabs>
        <Tab label="Active Containers"
             icon={<FontIcon className="material-icons">favorite</FontIcon>}
        >
          <ContainerList
            containers={this.props.containers}
            active={true}
            onView={this.handleViewContainer}
            onDeactivate={this.handleDeactivateContainer}
            onEdit={this.handleEditContainer}
            onDelete={this.handleDeleteContainer}
          />
        </Tab>
        <Tab label="Inactive Containers"
             icon={<FontIcon className="material-icons">do_not_disturb_alt</FontIcon>}
        >
          <ContainerList
            containers={this.props.containers}
            active={false}
            onActivate={this.handleActivateContainer}
            onEdit={this.handleEditContainer}
            onDelete={this.handleDeleteContainer}
          />
        </Tab>
      </Tabs>
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

export default connect(mapStateToProps, mapDispatchToProps)(ContainerMain);
