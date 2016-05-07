import {createAction} from "redux-actions";
import * as Config from "../store/Configuration";
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
  BULK_SET_LOGTYPE,
  BULK_SET_EXTRA
} from './ActionTypes';
import {push} from 'react-router-redux';

const fetchLogFilesRequest = createAction(
  FETCH_LOGFILES_REQUEST,
  containerId => containerId
);
const fetchLogFilesSuccess = createAction(
  FETCH_LOGFILES_SUCCESS,
  logFiles => logFiles
);
const fetchLogFilesFailure = createAction(
  FETCH_LOGFILES_FAILURE,
  ex=>ex.message
);

const bulkSetLogTypeAction = createAction(
  BULK_SET_LOGTYPE,
  (selectedRows, logType) => {
    return {selectedRows, logType}
  }
);
const bulkSetExtraAction = createAction(
  BULK_SET_EXTRA,
  (selectedRows, extra) => {
    return {selectedRows, extra}
  }
);

export function fetchLogFiles(containerId, searchParams) {
  console.log("fetchLogFiles!!" + searchParams);
  return dispatch => {
    dispatch(fetchLogFilesRequest());
    return fetch(Config.apiHost + "/api/containers/" + containerId + "/logfiles?" + searchParams)
      .then(res => res.json())
      .then(json => dispatch(fetchLogFilesSuccess(json)))
      .catch(ex => dispatch(fetchLogFilesFailure(ex)))
  }
}

export function changeCondition(containerId, searchParams) {
  return dispatch => {
    dispatch(push("/containers/" + containerId + "/logfiles?" + searchParams));
  }
}

export function bulkSetLogType(selectedRows, logType) {
  return dispatch => {
    dispatch(bulkSetLogTypeAction(selectedRows, logType));
  }
}

export function bulkSetExtra(selectedRows, extra) {
  return dispatch => {
    dispatch(bulkSetExtraAction(selectedRows, extra));
  }
}
