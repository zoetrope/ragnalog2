import React, {Component, PropTypes} from 'react';
import * as LogFileActions from "../../actions/LogFileAction";
import * as AppActions from "../../actions/AppAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import LogFileList from "./LogFileList";

class LogFileMain extends Component {

  constructor(props) {
    super(props);
    this.state = {
      tab: "Unregistered"
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
    });
    
    //TODO: use archive name instead of id
    const title = searchParams.has("archiveId") ? searchParams.get("archiveId") + "'s LogFiles" : this.props.params.containerId + "'s LogFiles";
    this.props.changeTitle(title);
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

  handleRegister = (selectedRows)=> {
    console.log("register: ", selectedRows);
    const targets = this.props.logFiles
      .filter((logFile, index)=> {
        return selectedRows === "all" || (selectedRows !== "none" && selectedRows.indexOf(index) !== -1)
      });
    this.props.registerLogFile(this.props.params.containerId, targets);
  };

  handleApplyFilter = (filterValue)=> {
    console.log("apply filter: ", filterValue);
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    searchParams.set("name", filterValue);
    this.props.changeCondition(this.props.params.containerId, searchParams.toString());
    this.props.fetchLogFiles(this.props.params.containerId, searchParams.toString());
  };

  handleSetLogType = (selectedRows, logType)=> {
    console.log("set log type: ", selectedRows, logType);
    this.props.bulkSetLogType(selectedRows, logType);
  };

  handleSetExtra = (selectedRows, extra)=> {
    console.log("set extra: ", selectedRows, extra);
    this.props.bulkSetExtra(selectedRows, extra);
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

  render() {
    return <div>
      <Tabs
        value={this.state.tab}
        onChange={this.handleTabChange}
      >
        <Tab label="Unregistered" value="Unregistered">
          <LogFileList
            logFiles={this.props.logFiles}
            onRegister={this.handleRegister}
            onSetLogType={this.handleSetLogType}
            onSetExtra={this.handleSetExtra}
            onApplyFilter={this.handleApplyFilter}
            onPageChange={this.handlePageChange}
            page={this.props.currentPage}
            limit={Math.ceil(this.props.totalCount/20) - 1}
            logTypes={this.props.logTypes}
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
    totalCount: state.LogFileReducer.totalCount,
    logTypes: state.AppReducer.logTypes
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({...LogFileActions, ...AppActions}, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(LogFileMain);
