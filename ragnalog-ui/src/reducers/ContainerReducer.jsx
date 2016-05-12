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
  UPDATE_CONTAINER_FAILURE,
  CHANGE_CONTAINERSTATUS_REQUEST,
  CHANGE_CONTAINERSTATUS_SUCCESS,
  CHANGE_CONTAINERSTATUS_FAILURE
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
    containers: [...state.containers, action.payload],
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
  [UPDATE_CONTAINER_SUCCESS]: (state, action) => {
    const index = state.containers.findIndex(c => c.id === action.payload.id);
    return ({
      containers: [
        ...state.containers.slice(0, index),
        action.payload,
        ...state.containers.slice(index + 1)
      ],
      isFetching: false,
      error: false,
      errorMessage: ""
    });
  },
  [UPDATE_CONTAINER_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [CHANGE_CONTAINERSTATUS_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [CHANGE_CONTAINERSTATUS_SUCCESS]: (state, action) => {
    const index = state.containers.findIndex(c => c.id === action.payload.id);
    return ({
      containers: [
        ...state.containers.slice(0, index),
        action.payload,
        ...state.containers.slice(index + 1)
      ],
      isFetching: false,
      error: false,
      errorMessage: ""
    });
  },
  [CHANGE_CONTAINERSTATUS_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [DELETE_CONTAINER_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [DELETE_CONTAINER_SUCCESS]: (state, action) => {
    const index = state.containers.findIndex(c => c.id === action.payload.id);
    return ({
      containers: [
        ...state.containers.slice(0, index),
        ...state.containers.slice(index + 1)
      ],
      isFetching: false,
      error: false,
      errorMessage: ""
    });
  },
  [DELETE_CONTAINER_FAILURE]: (state, action) => ({
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

