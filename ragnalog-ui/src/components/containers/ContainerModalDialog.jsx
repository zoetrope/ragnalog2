import React, {Component, PropTypes} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";
import TextField from 'material-ui/TextField';

class ContainerModalDialog extends Component {

  constructor(props) {
    super(props);
    this.state = {
      open: false,
      idFieldValue: "",
      nameFieldValue: "",
      descriptionFieldValue: "",
      idFieldErrorMessage: null,
      nameFieldErrorMessage: null
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.open) {
      this.setState({open: nextProps.open});
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
      if (this.validate(["name"])) {
        this.refs.descriptionField.focus();
      }
    }
  };
  handleKeyDownDescriptionField = (e) => {
    if (e.keyCode === 13) {
      this.handleSubmit()
    }
  };

  handleChangeIdField = (e)=> {
    this.setState({idFieldValue: e.target.value});
  };
  handleChangeNameField = (e)=> {
    this.setState({nameFieldValue: e.target.value});
  };
  handleChangeDescriptionField = (e)=> {
    this.setState({descriptionFieldValue: e.target.value});
  };

  handleSubmit = () => {
    if (this.validate(["id", "name"])) {
      this.props.onSubmit(this.state.idFieldValue, this.state.nameFieldValue, this.state.descriptionFieldValue);
      this.setState({open: false});
    }
  };

  validate = (fields) => {
    let valid = true;

    if (fields.includes("id")) {
      if (this.state.idFieldValue === "") {
        this.setState({idFieldErrorMessage: "ID is required"});
        valid = false;
      } else if (!/^[a-z0-9_]+$/.test(this.state.idFieldValue)) {
        this.setState({idFieldErrorMessage: "ID must be any alphabet, numeric, underscore."});
        valid = false;
      } else if (this.state.idFieldValue.length > 16) {
        this.setState({idFieldErrorMessage: "ID must be less than 16 characters."});
        valid = false;
      } else {
        this.setState({idFieldErrorMessage: ""});
      }
    }

    if (fields.includes("name")) {
      if (this.state.nameFieldValue === "") {
        this.setState({nameFieldErrorMessage: "Name is required"});
        valid = false;
      } else if (this.state.nameFieldValue.length > 16) {
        this.setState({nameFieldErrorMessage: "Name must be less than 16 characters."});
        valid = false;
      } else {
        this.setState({nameFieldErrorMessage: ""});
      }
    }

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
          hintText="ID (used as a part of URL)"
          value={this.state.idFieldValue}
          onKeyDown={this.handleKeyDownIdField}
          onChange={this.handleChangeIdField}
          errorText={this.state.idFieldErrorMessage}
        /><br />
        <TextField
          ref="nameField"
          hintText="Name (used as a display name)"
          value={this.state.nameFieldValue}
          onKeyDown={this.handleKeyDownNameField}
          onChange={this.handleChangeNameField}
          errorText={this.state.nameFieldErrorMessage}
        /><br />
        <TextField
          ref="descriptionField"
          hintText="Description"
          value={this.state.descriptionFieldValue}
          onKeyDown={this.handleKeyDownDescriptionField}
          onChange={this.handleChangeDescriptionField}
        /><br />
      </Dialog>
    </div>
  }
}

export default ContainerModalDialog;
