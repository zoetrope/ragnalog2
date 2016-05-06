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
  UNREGISTER_LOGFILE_FAILURE
} from './ActionTypes';
import {push} from 'react-router-redux';

const fetchLogFilesRequest = createAction(
  FETCH_LOGFILES_REQUEST,
  containerId => containerId
);
const fetchLogFilesSuccess = createAction(
  FETCH_LOGFILES_SUCCESS,
  archives => archives
);
const fetchLogFilesFailure = createAction(
  FETCH_LOGFILES_FAILURE,
  ex=>ex.message
);

export function fetchLogFiles(containerId, searchParams) {
  console.log("fetchLogFiles!!" + searchParams);
  return dispatch => {
    dispatch(fetchLogFilesRequest());
    return fetch(Config.apiHost + "/api/containers/" + containerId + "/logfiles" + searchParams)
      .then(res => res.json())
      .then(json => dispatch(fetchLogFilesSuccess(json)))
      .catch(ex => dispatch(fetchLogFilesFailure(ex)))
  }
}

export function navigateToViewLogFile(containerId, archiveId) {
  return dispatch => {
    dispatch(push("/containers/" + containerId + "/logfiles?archive=" + archiveId));
  }
}

