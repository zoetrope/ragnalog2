import {handleActions} from 'redux-actions';
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE,
  ADD_CONTAINER_REQUEST,
  ADD_CONTAINER_SUCCESS,
  ADD_CONTAINER_FAILURE,
  DELETE_CONTAINER_REQUEST,
  DELETE_CONTAINER_SUCCESS,
  DELETE_CONTAINER_FAILURE,
  UPDATE_CONTAINER_REQUEST,
  UPDATE_CONTAINER_SUCCESS,
  UPDATE_CONTAINER_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_CONTAINERS_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_CONTAINERS_SUCCESS]: (state, action) => ({
    containers: action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
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
    errorMessage: ""
  }),
  [ADD_CONTAINER_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [ADD_CONTAINER_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [UPDATE_CONTAINER_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [UPDATE_CONTAINER_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [UPDATE_CONTAINER_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  })
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  containers: []
});

