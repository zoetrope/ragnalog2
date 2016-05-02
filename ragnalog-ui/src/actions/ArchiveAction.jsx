import {createAction} from "redux-actions";
import * as Config from "../store/Configuration";
import {
  FETCH_ARCHIVES_REQUEST,
  FETCH_ARCHIVES_SUCCESS,
  FETCH_ARCHIVES_FAILURE,
  ADD_ARCHIVE_REQUEST,
  ADD_ARCHIVE_SUCCESS,
  ADD_ARCHIVE_FAILURE
} from './ActionTypes';
import {push} from 'react-router-redux';

const fetchArchivesRequest = createAction(
  FETCH_ARCHIVES_REQUEST,
  containerId => containerId
);
const fetchArchivesSuccess = createAction(
  FETCH_ARCHIVES_SUCCESS,
  archives => archives
);
const fetchArchivesFailure = createAction(
  FETCH_ARCHIVES_FAILURE,
  ex=>ex.message
);

export function fetchArchives(containerId) {
  console.log("fetchArchives!!");
  return dispatch => {
    dispatch(fetchArchivesRequest());
    return fetch(Config.apiHost + "/api/containers/" + containerId + "/archives")
      .then(res => res.json())
      .then(json => dispatch(fetchArchivesSuccess(json)))
      .catch(ex => dispatch(fetchArchivesFailure(ex)))
  }
}

