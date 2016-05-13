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

  componentWillReceiveProps(nextProps) {
    const dropArea = nextProps.dropArea;
    console.log("assignDrop", dropArea);
    this.flow.assignDrop(dropArea);
  }
  
  componentDidMount() {
    const uploadFileButton = ReactDOM.findDOMNode(this.refs.uploadFileButton);
    const uploadFolderButton = ReactDOM.findDOMNode(this.refs.uploadFolderButton);
    // const dropArea = ReactDOM.findDOMNode(this.refs.dropArea);

    //TODO: move to reducer?
    this.flow = new Flow({
        'target': (file, chunk) => {
          const target = Config.apiHost + '/api/containers/' + this.props.containerId + '/archives/' + file.uniqueIdentifier;
          console.log(target);
          return target;
        },
        "testChunks": false,
        "generateUniqueIdentifier": (file) => {
          const relativePath = file.relativePath || file.webkitRelativePath || file.fileName || file.name;
          return md5(this.props.containerId + relativePath + file.size + file.lastModifiedDate);
        },
        "query": (file) => {
          return {'lastModified': file.file.lastModifiedDate.toISOString()};
        }
      }
    );
    this.flow.assignBrowse(uploadFileButton);
    this.flow.assignBrowse(uploadFolderButton, true);
    this.flow.on('fileAdded', (file, event) => {
      console.log("fileAdded", file, event);
    });
    this.flow.on('fileSuccess', (file, message) => {
      console.log("fileSuccess", file, message);
      this.flow.removeFile(file);
      if (this.flow.files.length === 0) {
        this.setState({uploading: false});
      }
    });
    this.flow.on('fileError', (file, message) => {
      console.log("fileError", file, message);
      this.flow.removeFile(file);
      if (this.flow.files.length === 0) {
        this.setState({uploading: false});
      }
    });
    this.flow.on('filesSubmitted', (files, event) => {
      console.log("filesSubmitted", files, event);
      this.flow.upload();
      this.setState({uploading: true});
    });
    this.flow.on('progress', () => {
      console.log("progres", this.flow.progress());
      this.setState({completed: this.flow.progress() * 100});
    });
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
