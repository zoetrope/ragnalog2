import {handleActions} from 'redux-actions';
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_CONTAINERS_REQUEST]: state => (
    {
      isFetching: true,
      fetchingError: null
    }
  ),
  [FETCH_CONTAINERS_SUCCESS]: (state, action) => (
    {
      ...action.payload,
      isFetching: false,
      fetchingError: null
    }
  ),
  [FETCH_CONTAINERS_FAILURE]: (state, action) => (
    {
      isFetching: false,
      fetchingError: action.payload
    }
  )
}, {
  isFetching: false,
  fetchingError: null,
  containers: []
});

