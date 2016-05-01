import React, {Component, PropTypes} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";
import TextField from 'material-ui/TextField';

class ContainerModalDialog extends Component {

  constructor(props) {
    super(props);
    this.state = {open: false};
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.open) {
      this.setState({open: nextProps.open});
    }
  }

  componentDidUpdate() {
    if (this.state.open === true) {
      // this.refs.idField is either of correct instance or undefined in this timing.
      setTimeout(()=> {
        if (this.refs.idField) {
          this.refs.idField.focus();
        }
      }, 0);
    }
  }

  handleOpen = () => {
    this.setState({open: true});
  };

  handleClose = () => {
    this.setState({open: false});
  };

  handleKeyDownIdField = (e) => {
    if (e.keyCode === 13) {
      if (this.validate(["id"])) {
        this.refs.nameField.focus();
      }
    }
  };
  handleKeyDownNameField = (e) => {
    if (e.keyCode === 13) {
      if (this.validate(["description"])) {
        this.refs.descriptionField.focus();
      }
    }
  };
  handleKeyDownDescriptionField = (e) => {
    if (e.keyCode === 13) {
      this.handleSubmit()
    }
  };

  handleSubmit = () => {
    if (this.validate(["id", "description"])) {

      this.setState({open: false});
    }
  };

  validate = (fields) => {
    let valid = true;

    return valid;
  };

  render() {

    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onTouchTap={this.handleClose}
      />,
      <FlatButton
        label="Submit"
        primary={true}
        onTouchTap={this.handleSubmit}
      />
    ];

    return <div>
      <Dialog
        title="Add Container"
        ref="dialog"
        actions={actions}
        modal={true}
        open={this.state.open}
      >
        <TextField
          ref="idField"
          hintText="ID"
          onKeyDown={this.handleKeyDownIdField}
          errorText="This field is required"
        /><br />
        <TextField
          ref="nameField"
          hintText="Name"
          onKeyDown={this.handleKeyDownNameField}
          errorText="This field is required"
        /><br />
        <TextField
          ref="descriptionField"
          hintText="Description"
          onKeyDown={this.handleKeyDownDescriptionField}
        /><br />
      </Dialog>
    </div>
  }
}

export default ContainerModalDialog;
