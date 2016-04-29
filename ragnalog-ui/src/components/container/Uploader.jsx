import React, {Component, PropTypes} from 'react';
import ReactDOM from "react-dom";
import RaisedButton from "material-ui/RaisedButton";
import FontIcon from "material-ui/FontIcon";
import Flow from "@flowjs/flow.js"
import Paper from 'material-ui/Paper';
import * as theme from "../../RagnalogTheme";

const style = {
  margin: 12
};

const paperStyle = {
  height: 200,
  width: 800,
  margin: 20,
  textAlign: 'center',
  display: 'inline-block',
  background: theme.palette.primary2Color
};

const serverHost = "http://localhost:8686"; //TODO: set to store?

class Uploader extends Component {

  componentDidMount() {
    const uploadFileButton = ReactDOM.findDOMNode(this.refs.uploadFileButton);
    const uploadFolderButton = ReactDOM.findDOMNode(this.refs.uploadFolderButton);
    const dropArea = ReactDOM.findDOMNode(this.refs.dropArea);

    const flow = new Flow({
        'target': (file, chunk) => {
          // const target = serverHost + '/api/container/' + this.containerId + '/archive/' + file.uniqueIdentifier;
          const target = serverHost + '/api/archive/';
          console.log(target);
          return target;
        },
        "testChunks": false
      }
    );
    flow.assignBrowse(uploadFileButton);
    flow.assignBrowse(uploadFolderButton, true);
    flow.assignDrop(dropArea);
    flow.on('fileAdded', function (file, event) {
      console.log("fileAdded", file, event);
    });
    flow.on('fileSuccess', function (file, message) {
      console.log("fileSuccess", file, message);
    });
    flow.on('fileError', function (file, message) {
      console.log("fileError", file, message);
    });
    flow.on('filesSubmitted', function (files, event) {
      console.log("filesSubmitted", files, event);
      flow.upload();
    });
    // this.props.flow.assignBrowse(uploadFileButton);
    // this.props.flow.assignDrop(uploadFolderButton);
  }

  render() {
    return <div>
      <Paper ref="dropArea" style={paperStyle} zDepth={2}>
        <h1><FontIcon className="material-icons">cloud_upload</FontIcon> Upload File</h1>

        <p>
          Drop files here or click the button below to upload
        </p>

        <RaisedButton
          ref="uploadFileButton"
          label="File" style={style}
          primary={true}
          icon={<FontIcon className="material-icons">insert_drive_file</FontIcon>}
        />
        <RaisedButton
          ref="uploadFolderButton"
          label="Folder" style={style}
          primary={true}
          icon={<FontIcon className="material-icons">folder</FontIcon>}
        />
      </Paper>
    </div>
  }
}

export default Uploader;
