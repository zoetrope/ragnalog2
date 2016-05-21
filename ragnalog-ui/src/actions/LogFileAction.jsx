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
  PREVIEW_LOGFILE_REQUEST,
  PREVIEW_LOGFILE_SUCCESS,
  PREVIEW_LOGFILE_FAILURE,
  CLOSE_PREVIEW_DIALOG,
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

const registerLogFileRequest = createAction(
  REGISTER_LOGFILE_REQUEST,
  (id, logType, extra) => {
    return {
      id,
      logType,
      extra
    }
  }
);
const registerLogFileSuccess = createAction(
  REGISTER_LOGFILE_SUCCESS,
  res => res
);
const registerLogFileFailure = createAction(
  REGISTER_LOGFILE_FAILURE,
  ex=>ex.message
);

export function registerLogFile(containerId, targets) {
  console.log("registerLogFile!!", containerId, targets);
  return dispatch => {
    dispatch(registerLogFileRequest());
    return fetch(Config.apiHost + "/api/containers/" + containerId + "/logfiles", {
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(
        targets.map(target=> {
          return {
            id: target.id,
            archiveId: target.archiveId,
            logType: target.logType,
            extra: target.extra || undefined
          }
        })
      )
    })
      .then(res => res.json())
      .then(json => dispatch(registerLogFileSuccess(json)))
      .catch(ex => dispatch(registerLogFileFailure(ex)))
  }
}

const previewLogFileRequest = createAction(
  PREVIEW_LOGFILE_REQUEST,
  containerId => containerId
);
const previewLogFileSuccess = createAction(
  PREVIEW_LOGFILE_SUCCESS,
  content => content
);
const previewLogFileFailure = createAction(
  PREVIEW_LOGFILE_FAILURE,
  ex=>ex.message
);

export function previewLogFile(containerId, logFile) {
  console.log("previewLogFile!!" + logFile);
  return dispatch => {
    dispatch(previewLogFileRequest());
    return fetch(Config.apiHost + "/api/containers/" + containerId + "/logfiles/" + logFile.id + "?archiveId=" + logFile.archiveId)
      .then(res => res.json())
      .then(json => dispatch(previewLogFileSuccess(json)))
      .catch(ex => dispatch(previewLogFileFailure(ex)))
  }
}

const closePreviewDialogAction = createAction(
  CLOSE_PREVIEW_DIALOG
);

export function closePreviewDialog() {
  return dispatch => {
    dispatch(closePreviewDialogAction());
  }
}

const bulkSetLogTypeAction = createAction(
  BULK_SET_LOGTYPE,
  (selectedRows, logType) => {
    return {selectedRows, logType}
  }
);

export function bulkSetLogType(selectedRows, logType) {
  return dispatch => {
    dispatch(bulkSetLogTypeAction(selectedRows, logType));
  }
}

const bulkSetExtraAction = createAction(
  BULK_SET_EXTRA,
  (selectedRows, extra) => {
    return {selectedRows, extra}
  }
);

export function bulkSetExtra(selectedRows, extra) {
  return dispatch => {
    dispatch(bulkSetExtraAction(selectedRows, extra));
  }
}

export function changeCondition(containerId, searchParams) {
  return dispatch => {
    dispatch(push("/containers/" + containerId + "/logfiles?" + searchParams));
  }
}
