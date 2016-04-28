import React, {Component, PropTypes} from 'react';
import ReactDOM from "react-dom";
import RaisedButton from "material-ui/RaisedButton";
import FontIcon from "material-ui/FontIcon";
import Flow from "@flowjs/flow.js"

const style = {
  margin: 12
};


class Uploader extends Component {

  componentDidMount() {
    const uploadFileButton = ReactDOM.findDOMNode(this.refs.uploadFileButton),
      uploadFolderButton = ReactDOM.findDOMNode(this.refs.uploadFolderButton);

    const flow = new Flow();
    flow.assignBrowse(uploadFileButton);
    flow.assignDrop(uploadFolderButton);

    // this.props.flow.assignBrowse(uploadFileButton);
    // this.props.flow.assignDrop(uploadFolderButton);
  }

  render() {
    return <div>
      <RaisedButton
        ref="uploadFileButton"
        label="Upload File" style={style}
        primary={true}
        icon={<FontIcon className="material-icons">insert_drive_file</FontIcon>}
      />
      <RaisedButton
        ref="uploadFolderButton"
        label="Upload Folder" style={style}
        primary={true}
        icon={<FontIcon className="material-icons">folder</FontIcon>}
      />
    </div>
  }
}

export default Uploader;
