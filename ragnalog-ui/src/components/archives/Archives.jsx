import React, {Component, PropTypes} from 'react';
import Uploader from "./Uploader"
import * as Actions from "../../actions/ArchiveAction";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {Tabs, Tab} from "material-ui/Tabs";
import ArchiveList from "./ArchiveList";

class Archives extends Component {

  constructor(props) {
    super(props);
  }

  componentWillMount() {
    console.log("Archives will mount", this.props.params);

    this.props.fetchArchives(this.props.params.id);
  }

  componentWillReceiveProps(nextProps) {
    console.log("Archives will receive props", nextProps.params)
  }

  render() {
    return <div>
      <Uploader containerId={this.props.params.id}/>
      <ArchiveList archives={this.props.archives}/>
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

export default connect(mapStateToProps, mapDispatchToProps)(Archives);
