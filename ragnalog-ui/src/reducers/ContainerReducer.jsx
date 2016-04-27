import {handleActions} from 'redux-actions';
import Immutable from 'immutable';
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_CONTAINERS_REQUEST]: state => state.merge(
    Immutable.Map({
      isFetching: true,
      fetchingError: null
    })
  ),
  [FETCH_CONTAINERS_SUCCESS]: (state, action) => state.merge(
    Immutable.Map({
      ...action.payload,
      isFetching: false,
      fetchingError: null
    })
  ),
  [FETCH_CONTAINERS_FAILURE]: (state, action) => state.merge(
    Immutable.Map({
      isFetching: false,
      fetchingError: action.payload
    })
  )
}, Immutable.fromJS({
  isFetching: false,
  fetchingError: null,
  containers: []
}));
