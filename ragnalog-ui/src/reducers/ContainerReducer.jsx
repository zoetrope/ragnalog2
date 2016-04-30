import {handleActions} from 'redux-actions';
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_CONTAINERS_REQUEST]: state => (
  {
    ...state,
    isFetching: true,
    hasError: false,
    fetchingError: null
  }
  ),
  [FETCH_CONTAINERS_SUCCESS]: (state, action) => (
  {
    ...action.payload,
    isFetching: false,
    hasError: false,
    fetchingError: null
  }
  ),
  [FETCH_CONTAINERS_FAILURE]: (state, action) => (
  {
    ...state,
    isFetching: false,
    hasError: true,
    fetchingError: action.payload
  }
  )
}, {
  isFetching: false,
  hasError: false,
  fetchingError: null,
  containers: []
});

