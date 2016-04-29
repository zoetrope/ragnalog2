import React, {Component, PropTypes} from 'react';
import Uploader from "./Uploader"

class Container extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    return <div>
      <Uploader />
    </div>
  }
}

export default Container;
