import {handleActions} from 'redux-actions';
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE,
  ADD_CONTAINER_REQUEST,
  ADD_CONTAINER_SUCCESS,
  ADD_CONTAINER_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_CONTAINERS_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: null
  }),
  [FETCH_CONTAINERS_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: null
  }),
  [FETCH_CONTAINERS_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [ADD_CONTAINER_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: null
  }),
  [ADD_CONTAINER_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: null
  }),
  [ADD_CONTAINER_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  })
}, {
  isFetching: false,
  error: false,
  errorMessage: null,
  containers: []
});

