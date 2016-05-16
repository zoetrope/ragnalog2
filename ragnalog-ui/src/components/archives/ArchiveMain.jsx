import React, {Component, PropTypes} from "react";
import ReactDOM from "react-dom";
import Uploader from "./Uploader";
import * as Actions from "../../actions/ArchiveAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import ArchiveList from "./ArchiveList";
import TextField from "material-ui/TextField";

const styles = {
  dropArea: {
    height: "100%"
  },
  controlBar: {
    margin: "0 20px 20px"
  }
};
class ArchiveMain extends Component {

  constructor(props) {
    super(props);
    this.state = {
      dropArea: {},
      filterValue: ""
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

  handleDeleteArchive = (archive)=> {
    this.props.deleteArchive(this.props.params.containerId, archive.id);
  };

  handleFilterValueChange = (e) => {
    this.setState({filterValue: e.target.value});
    e.stopPropagation();
  };

  filterArchive = (archive) => {
    return archive.fileName.indexOf(this.state.filterValue) !== -1;
  };

  render() {

    return <div ref="dropArea" style={styles.dropArea}>
      <div style={styles.controlBar}>
      <span>
        <TextField
          hintText="filter"
          value={this.state.filterValue}
          onChange={this.handleFilterValueChange}
        />
      </span>
        <Uploader
          containerId={this.props.params.containerId}
          dropArea={this.state.dropArea}
        />
      </div>
      <ArchiveList
        archives={this.props.archives.filter(this.filterArchive)}
        onView={this.handleViewContainer}
        onDelete={this.handleDeleteArchive}
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
