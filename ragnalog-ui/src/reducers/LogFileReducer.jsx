import {handleActions} from 'redux-actions';
import {
  FETCH_LOGFILES_REQUEST,
  FETCH_LOGFILES_SUCCESS,
  FETCH_LOGFILES_FAILURE,
  REGISTER_LOGFILE_REQUEST,
  REGISTER_LOGFILE_SUCCESS,
  REGISTER_LOGFILE_FAILURE,
  DELETE_LOGFILE_REQUEST,
  DELETE_LOGFILE_SUCCESS,
  DELETE_LOGFILE_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_LOGFILES_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_LOGFILES_SUCCESS]: (state, action) => {
    console.log("fetch logfiles: ", state);
    return ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  })},
  [FETCH_LOGFILES_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [REGISTER_LOGFILE_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [REGISTER_LOGFILE_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [REGISTER_LOGFILE_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  })
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  logFiles: []
});
