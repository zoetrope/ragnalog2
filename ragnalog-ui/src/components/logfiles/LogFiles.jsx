import React, {Component, PropTypes} from 'react';
import * as Actions from "../../actions/LogFileAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import LogFileList from "./LogFileList";

class LogFiles extends Component {

  constructor(props) {
    super(props);
    this.state = {
      tab: "Unregistered"
    }
  }

  componentWillMount() {
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
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

  handleChange = (tab)=> {
    const searchParams = new URLSearchParams(this.props.location.search.slice(1));
    searchParams.set("status", tab);

    this.setState({
      tab: tab
    });

    console.log("handleChange", searchParams.toString());
    this.props.changeCondition(this.props.params.containerId, searchParams.toString());
    this.props.fetchLogFiles(this.props.params.containerId, searchParams.toString());
  };

  render() {
    return <div>
      <Tabs
        value={this.state.tab}
        onChange={this.handleChange}
      >
        <Tab label="Unregistered" value="Unregistered">
          <LogFileList
            logFiles={this.props.logFiles}
            onRegister={this.handleRegister}
            onUnregister={this.handleUnregister}
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
    logFiles: state.LogFileReducer.logFiles
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(LogFiles);
