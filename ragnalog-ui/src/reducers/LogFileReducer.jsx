import {handleActions} from 'redux-actions';
import {
  FETCH_LOGFILES_REQUEST,
  FETCH_LOGFILES_SUCCESS,
  FETCH_LOGFILES_FAILURE,
  REGISTER_LOGFILE_REQUEST,
  REGISTER_LOGFILE_SUCCESS,
  REGISTER_LOGFILE_FAILURE,
  UNREGISTER_LOGFILE_REQUEST,
  UNREGISTER_LOGFILE_SUCCESS,
  UNREGISTER_LOGFILE_FAILURE,
  PREVIEW_LOGFILE_REQUEST,
  PREVIEW_LOGFILE_SUCCESS,
  PREVIEW_LOGFILE_FAILURE,
  CLOSE_PREVIEW_DIALOG,
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
    ...state,
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
    ...state,
    // ...action.payload,
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
  [PREVIEW_LOGFILE_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [PREVIEW_LOGFILE_SUCCESS]: (state, action) => ({
    ...state,
    isFetching: false,
    error: false,
    errorMessage: "",
    preview: true,
    previewContent: action.payload.content
  }),
  [PREVIEW_LOGFILE_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [CLOSE_PREVIEW_DIALOG]: (state, action) => ({
    ...state,
    preview: false
  }),
  [BULK_SET_LOGTYPE]: (state, action) => {
    const {selectedRows, logType} = action.payload;
    return {
      ...state,
      logFiles: state.logFiles.map((logFile, index)=> {
        if (selectedRows === "all" || (selectedRows !== "none" && selectedRows.indexOf(index) !== -1)) {
          return Object.assign({}, logFile, {
            logType: logType,
            selected: true
          });
        }
        return logFile;
      }),
      currentPage: state.currentPage,
      totalCount: state.totalCount,
      isFetching: false,
      error: false,
      errorMessage: ""
    }
  },
  [BULK_SET_EXTRA]: (state, action) => {
    const {selectedRows, extra} = action.payload;
    return {
      ...state,
      logFiles: state.logFiles.map((logFile, index)=> {
        if (selectedRows === "all" || (selectedRows !== "none" && selectedRows.indexOf(index) !== -1)) {
          return Object.assign({}, logFile, {
            extra: extra,
            selected: true
          });
        }
        return logFile;
      }),
      currentPage: state.currentPage,
      totalCount: state.totalCount,
      isFetching: false,
      error: false,
      errorMessage: ""
    }
  }
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  preview: false,
  previewContent: "",
  logFiles: [],
  currentPage: 0,
  totalCount: 0
});
