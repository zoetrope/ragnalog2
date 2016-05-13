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

const styles = {
  rightButton: {
    margin: "5px 0",
    float: 'right'
  },
  buttonGroup: {
    margin: "5px 10px"
  }
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
    updateContainer: PropTypes.func.isRequired,
    changeStatus: PropTypes.func.isRequired,
    deleteContainer: PropTypes.func.isRequired
  };

  constructor(props) {
    super(props);
    this.state = {
      openMessage: false,
      openDialog: false,
      create: false,
      target: null,
      tab: "active"
    };
  }

  componentWillMount() {
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    const status = searchParams.get("status") || "active";
    this.props.fetchContainers(status);
    this.setState({
      tab: status
    })
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      openMessage: nextProps.error,
      openDialog: nextProps.openDialog
    });
  }

  handleOpenDialog = () => {
    this.setState({
      openDialog: true,
      create: true,
      target: null
    });
  };

  handleCloseMessage = () => {
    this.setState({
      openMessage: false
    });
  };

  handleTabChange = (tab)=> {
    console.log("handleChange", tab);

    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    searchParams.set("status", tab);

    this.setState({
      tab: tab
    });

    this.props.changeStatus(tab);
    this.props.fetchContainers(tab);
  };

  handleViewContainer = (container)=> {
    this.props.navigateToViewContainer(container.id)
  };

  handleActivateContainer = (container)=> {
    this.props.changeContainerStatus(container.id, "active");
  };

  handleDeactivateContainer = (container) => {
    this.props.changeContainerStatus(container.id, "inactive");
  };

  handleEditContainer = (container)=> {
    this.setState({
      openDialog: true,
      create: false,
      target: container
    });
  };

  handleDeleteContainer = (container)=> {
    this.props.deleteContainer(container.id);
  };

  render() {

    return <div>
      <div style={styles.buttonGroup}>
        <TextField hintText="Filter"/>
        <RaisedButton
          label="Add Container" style={styles.rightButton}
          icon={<FontIcon className="material-icons">add_circle</FontIcon>}
          onTouchTap={this.handleOpenDialog}
        />
        <ContainerModalDialog
          open={this.state.openDialog}
          create={this.state.create}
          target={this.state.target}
          onSubmit={(id, name, desc) => this.state.create ? this.props.addContainer(id, name, desc) : this.props.updateContainer(id, name, desc)}
        />
      </div>
      <Tabs
        value={this.state.tab}
        onChange={this.handleTabChange}
      >
        <Tab label="Active Containers"
             value="active"
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
             value="inactive"
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
  return {
    isFetching: state.ContainerReducer.isFetching,
    error: state.ContainerReducer.error,
    errorMessage: state.ContainerReducer.errorMessage,
    containers: state.ContainerReducer.containers,
    openDialog: state.ContainerReducer.openDialog
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(ContainerMain);
