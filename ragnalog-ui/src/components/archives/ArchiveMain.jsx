import React, {Component, PropTypes} from "react";
import ReactDOM from "react-dom";
import Uploader from "./Uploader";
import * as ArchiveActions from "../../actions/ArchiveAction";
import * as AppActions from "../../actions/AppAction";
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

    //TODO: use container name instead of id
    this.props.changeTitle(this.props.params.containerId + "'s Archives");
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
  
  static propTypes = {
    isFetching: PropTypes.bool.isRequired,
    error: PropTypes.bool.isRequired,
    errorMessage: PropTypes.node.isRequired,
    archives: PropTypes.arrayOf(
      PropTypes.shape({
        id: PropTypes.string.isRequired,
        fileName: PropTypes.string.isRequired,
        size: PropTypes.number,
        uploadedDate: PropTypes.string,
        modifiedDate: PropTypes.string
      })
    ),

    fetchArchives: PropTypes.func.isRequired,
    deleteArchive: PropTypes.func.isRequired,
    navigateToViewArchive: PropTypes.func.isRequired,
    
    changeTitle: PropTypes.func.isRequired
  };
}

function mapStateToProps(state) {
  return {
    archives: state.ArchiveReducer.archives
  };
}

function mapDispatchToProps(dispatch) {
  return bindActionCreators({...ArchiveActions, ...AppActions}, dispatch);
}

export default connect(mapStateToProps, mapDispatchToProps)(ArchiveMain);
