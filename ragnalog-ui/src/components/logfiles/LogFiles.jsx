import React, {Component, PropTypes} from 'react';
import * as Actions from "../../actions/LogFileAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import LogFileList from "./LogFileList";

class LogFiles extends Component {

  constructor(props) {
    super(props);
    // this.setState({
    //   tab: "Unregistered"
    // })
  }

  componentWillMount() {
    console.log("Archives will mount", this.props.params, this.props.location.search);

    this.props.fetchLogFiles(this.props.params.containerId, this.props.location.search);
  }

  componentWillReceiveProps(nextProps) {
    console.log("Archives will receive props", nextProps.params)
  }

  handleRegister = (logfile)=> {
  };

  handleUnregister = (logfile)=> {
  };

  render() {
    return <div>
      <Tabs>
        <Tab label="Unregistered">
          <LogFileList
            logFiles={this.props.logFiles}
            onRegister={this.handleRegister}
            onUnregister={this.handleUnregister}
          />
        </Tab>
        <Tab label="Registering">
        </Tab>
        <Tab label="Registered">
        </Tab>
        <Tab label="Error">
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
