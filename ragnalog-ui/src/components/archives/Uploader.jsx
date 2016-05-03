import React, {Component, PropTypes} from "react";
import ReactDOM from "react-dom";
import RaisedButton from "material-ui/RaisedButton";
import FontIcon from "material-ui/FontIcon";
import Paper from "material-ui/Paper";
import LinearProgress from "material-ui/LinearProgress";
import * as theme from "../../RagnalogTheme";
import Flow from "@flowjs/flow.js";
import * as Config from "../../store/Configuration";
import md5 from "blueimp-md5"

const styles = {
  button: {
    margin: 12
  },
  paper: {
    height: 200,
    width: 800,
    margin: 20,
    textAlign: 'center',
    display: 'inline-block',
    background: theme.palette.primary2Color
  }
};

class Uploader extends Component {

  constructor(props) {
    super(props);
    this.state = {
      uploading: false,
      completed: 0.0
    };
  }

  componentDidMount() {
    const uploadFileButton = ReactDOM.findDOMNode(this.refs.uploadFileButton);
    const uploadFolderButton = ReactDOM.findDOMNode(this.refs.uploadFolderButton);
    const dropArea = ReactDOM.findDOMNode(this.refs.dropArea);

    //TODO: move to reducer?
    const flow = new Flow({
        'target': (file, chunk) => {
          const target = Config.apiHost + '/api/containers/' + this.props.containerId + '/archives/' + file.uniqueIdentifier;
          console.log(target);
          return target;
        },
        "testChunks": false,
        "generateUniqueIdentifier": (file) => {
          const relativePath = file.relativePath || file.webkitRelativePath || file.fileName || file.name;
          return md5(relativePath + file.size + file.lastModifiedDate);
        },
        "query": (file) => {
          return {'lastModified': file.file.lastModifiedDate.toISOString()};
        }
      }
    );
    flow.assignBrowse(uploadFileButton);
    flow.assignBrowse(uploadFolderButton, true);
    flow.assignDrop(dropArea);
    flow.on('fileAdded', (file, event) => {
      console.log("fileAdded", file, event);
    });
    flow.on('fileSuccess', (file, message) => {
      console.log("fileSuccess", file, message);
      flow.removeFile(file);
      if (flow.files.length === 0) {
        this.setState({uploading: false});
      }
    });
    flow.on('fileError', (file, message) => {
      console.log("fileError", file, message);
      flow.removeFile(file);
      if (flow.files.length === 0) {
        this.setState({uploading: false});
      }
    });
    flow.on('filesSubmitted', (files, event) => {
      console.log("filesSubmitted", files, event);
      flow.upload();
      this.setState({uploading: true});
    });
    flow.on('progress', () => {
      console.log("progres", flow.progress());
      this.setState({completed: flow.progress() * 100});
    });
    // this.props.flow.assignBrowse(uploadFileButton);
    // this.props.flow.assignDrop(uploadFolderButton);
  }

  render() {

    const progress = this.state.uploading ?
      <LinearProgress mode="determinate" value={this.state.completed}/> : null;

    return <div>
      <Paper ref="dropArea" style={styles.paper} zDepth={2}>
        <h1><FontIcon className="material-icons">cloud_upload</FontIcon> Upload File</h1>

        <p>
          Drop files here or click the button below to upload
        </p>

        <RaisedButton
          ref="uploadFileButton"
          label="File" style={styles.button}
          primary={true}
          icon={<FontIcon className="material-icons">insert_drive_file</FontIcon>}
        />
        <RaisedButton
          ref="uploadFolderButton"
          label="Folder" style={styles.button}
          primary={true}
          icon={<FontIcon className="material-icons">folder</FontIcon>}
        />
      </Paper>
      {progress}
    </div>
  }
}

export default Uploader;