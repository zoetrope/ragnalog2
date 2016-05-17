import {createAction} from "redux-actions";
import * as Config from "../store/Configuration";
import {
  FETCH_LOGTYPES_REQUEST,
  FETCH_LOGTYPES_SUCCESS,
  FETCH_LOGTYPES_FAILURE,
  CHANGE_TITLE
} from '../actions/ActionTypes';
import {push} from 'react-router-redux';

const fetchLogTypessRequest = createAction(
  FETCH_LOGTYPES_REQUEST
);
const fetchLogTypesSuccess = createAction(
  FETCH_LOGTYPES_SUCCESS,
  logTypes => logTypes
);
const fetchLogTypesFailure = createAction(
  FETCH_LOGTYPES_FAILURE,
  ex=>ex.message
);
const changeTitleAction = createAction(
  CHANGE_TITLE,
  title => title
);

export function fetchLogTypes() {
  return dispatch => {
    dispatch(fetchLogTypessRequest());
    return fetch(Config.apiHost + "/api/logtypes")
      .then(res => res.json())
      .then(json => dispatch(fetchLogTypesSuccess(json)))
      .catch(ex => dispatch(fetchLogTypesFailure(ex)))
  }
}

export function changeTitle(title) {
  return dispatch => {
    document.title = title + " - Ragnalog";
    dispatch(changeTitleAction(title));
  }
}

