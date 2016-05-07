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
  DELETE_LOGFILE_FAILURE,
  BULK_SET_LOGTYPE,
  BULK_SET_EXTRA
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_LOGFILES_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_LOGFILES_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
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
  }),
  [BULK_SET_LOGTYPE]: (state, action) => {
    const {selectedRows, logType} = action.payload;
    return {
      logFiles: state.logFiles.map((logFile, index)=> {
        if (selectedRows === "all" || (selectedRows !== "none" && selectedRows.indexOf(index) !== -1)) {
          return Object.assign({}, logFile, {
            logType: logType
          });
        }
        return logFile;
      }),
      isFetching: false,
      error: false,
      errorMessage: ""
    }
  },
  [BULK_SET_EXTRA]: (state, action) => {
    const {selectedRows, extra} = action.payload;
    return {
      logFiles: state.logFiles.map((logFile, index)=> {
        if (selectedRows === "all" || (selectedRows !== "none" && selectedRows.indexOf(index) !== -1)) {
          return Object.assign({}, logFile, {
            extra: extra
          });
        }
        return logFile;
      }),
      isFetching: false,
      error: false,
      errorMessage: ""
    }
  }
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  logFiles: []
});
