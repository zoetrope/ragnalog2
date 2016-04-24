import React, {Component, PropTypes} from "react";
import Dialog from "material-ui/Dialog";
import FlatButton from "material-ui/FlatButton";

class ContainerModalDialog extends Component {

  constructor(props) {
    super(props);
    this.state = {open: false};
  }

  handleOpen = () => {
    this.setState({open: true});
  };

  handleClose = () => {
    this.setState({open: false});
  };

  componentWillReceiveProps(nextProps) {
    if(nextProps.open) {
      this.setState({open: nextProps.open});
    }
  }

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
        disabled={true}
        onTouchTap={this.handleClose}
      />
    ];


    return <div>
      <Dialog
        title="Dialog With Actions"
        actions={actions}
        modal={true}
        open={this.state.open}
      >
        Only actions can close this dialog.
      </Dialog>
    </div>
  }
}

export default ContainerModalDialog;
