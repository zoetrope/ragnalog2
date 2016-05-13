import React, {Component, PropTypes} from 'react';
import ReactDOM from "react-dom";
import Uploader from "./Uploader"
import * as Actions from "../../actions/ArchiveAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import ArchiveList from "./ArchiveList";

const styles = {
  dropArea: {
    height: "100%"
  }
};
class ArchiveMain extends Component {

  constructor(props) {
    super(props);
    this.state = {
      dropArea: {}
    }
  }

  componentWillMount() {
    console.log("Archives will mount", this.props.params);

    this.props.fetchArchives(this.props.params.containerId);
  }

  componentDidMount() {
    const dropArea = ReactDOM.findDOMNode(this.refs.dropArea);
    this.setState({
      dropArea: dropArea
    });
  }

  componentWillReceiveProps(nextProps) {
    console.log("Archives will receive props", nextProps.params)
  }

  handleViewContainer = (archive)=> {
    this.props.navigateToViewArchive(this.props.params.containerId, archive.id)
  };

  handleDeleteContainer = (archive)=> {
  };

  render() {

    return <div ref="dropArea" style={styles.dropArea}>
      <Uploader
        containerId={this.props.params.containerId}
        dropArea={this.state.dropArea}
      />
      <ArchiveList
        archives={this.props.archives}
        onView={this.handleViewContainer}
        onDelete={this.handleDeleteContainer}
      />
    </div>
  }
}

function mapStateToProps(state) {
  return {
    archives: state.ArchiveReducer.archives
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators(Actions, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(ArchiveMain);
