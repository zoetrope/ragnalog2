import React, {PropTypes, Component} from "react";
import {AppBar, Styles} from "material-ui";
import {Link} from 'react-router'
import IconButton from "material-ui/IconButton";
import ChevronLeft from 'material-ui/svg-icons/navigation/chevron-left';
import ChevronRight from 'material-ui/svg-icons/navigation/chevron-right';

const styles = {
  float: 'right'
};

class Pagination extends Component {

  constructor(props) {
    super(props);
  }

  render() {

    const {page, limit} = this.props;

    return (
      <div style={styles}>
        <IconButton disabled={page === 0} onClick={() => this.props.onPageChange(page - 1)}>
          <ChevronLeft/>
        </IconButton>
        <IconButton disabled={page >= limit} onClick={() => this.props.onPageChange(page + 1)}>
          <ChevronRight/>
        </IconButton>
        {page + ' of ' + limit}
      </div>
    );
  }
}

export default Pagination;
