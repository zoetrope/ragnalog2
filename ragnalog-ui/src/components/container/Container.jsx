import React, {Component, PropTypes} from 'react';
import Uploader from "./Uploader"
import {Link} from 'react-router'

class Container extends Component {

  render() {
    return <div>
      <Uploader />
      <ul>
        <li><Link to="/">Home</Link></li>
        <li><Link to="/todos">TODOS</Link></li>
        <li><Link to="/hello">Hello</Link></li>
      </ul>
    </div>
  }
}

export default Container;
