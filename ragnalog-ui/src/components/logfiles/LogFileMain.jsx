import React, {Component, PropTypes} from 'react';
import * as Actions from "../../actions/LogFileAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import LogFileList from "./LogFileList";
import TextField from 'material-ui/TextField';
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

class LogFileMain extends Component {

  constructor(props) {
    super(props);
    this.state = {
      tab: "Unregistered",
      openBulkSetDialog: false
    }
  }

  componentWillMount() {
    //TODO: for Development
    this.props.params.containerId = "test";
    const testParam = "archiveId=2548922f3dfe82e4e86b2154c91d0a14&status=Unregistered";
    const searchParams = new URLSearchParams(testParam);
    
    // const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    console.log("Archives will mount", this.props.params, searchParams.toString());
    this.props.fetchLogFiles(this.props.params.containerId, searchParams.toString());
    const status = searchParams.get("status");
    this.setState({
      tab: status
    })
  }

  componentWillReceiveProps(nextProps) {
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    console.log("Archives will receive props", nextProps.params, searchParams.toString());
    // this.props.fetchLogFiles(this.props.params.containerId, this.props.location.search);

    // const status = searchParams.get("status");
    // this.setState({
    //   tab: status
    // })
  }

  handleRegister = (logfile)=> {
  };

  handleUnregister = (logfile)=> {
  };

  handleTabChange = (tab)=> {
    console.log("handleChange", tab);

    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    searchParams.set("status", tab);

    this.setState({
      tab: tab
    });

    this.props.changeCondition(this.props.params.containerId, searchParams.toString());
    this.props.fetchLogFiles(this.props.params.containerId, searchParams.toString());
  };

  handlePageChange = (page)=> {
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    searchParams.set("page", page);
    this.props.changeCondition(this.props.params.containerId, searchParams.toString());
    this.props.fetchLogFiles(this.props.params.containerId, searchParams.toString());
  };

  handleOpenBulkSetDialog = () => {
    this.setState({openBulkSetDialog: true})
  };
  handleCloseBulkSetDialog = () => {
    this.setState({openBulkSetDialog: false})
  };

  render() {
    return <div>
      <Tabs
        value={this.state.tab}
        onChange={this.handleTabChange}
      >
        <Tab label="Unregistered" value="Unregistered">
          <div style={styles.buttonGroup}>
            <TextField hintText="filter"/>
            <FlatButton
              label="Filter" style={styles.button}
            />
            <RaisedButton
              label="Register" style={styles.rightButton}
              primary={true}
              icon={<FontIcon className="material-icons">add_circle</FontIcon>}
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
              <TextField hintText="log type"/>
              <FlatButton label="set"/>
              <br/>
              <TextField hintText="extra"/>
              <FlatButton label="set"/>
            </Dialog>
          </div>

          <LogFileList
            logFiles={this.props.logFiles}
            onRegister={this.handleRegister}
            onUnregister={this.handleUnregister}
            onPageChange={this.handlePageChange}
            page={this.props.currentPage}
            limit={Math.ceil(this.props.totalCount/100) - 1}
          />
        </Tab>
        <Tab label="Registering" value="Registering">
        </Tab>
        <Tab label="Registered" value="Registered">
        </Tab>
        <Tab label="Error" value="Error">
        </Tab>
      </Tabs>
    </div>
  }
}

function mapStateToProps(state) {
  return {
    logFiles: state.LogFileReducer.logFiles,
    currentPage: state.LogFileReducer.currentPage,
    totalCount: state.LogFileReducer.totalCount
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(LogFileMain);
