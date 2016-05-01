import {createAction} from "redux-actions";
import * as Config from "../store/Configuration";
import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE,
  ADD_CONTAINER_REQUEST,
  ADD_CONTAINER_SUCCESS,
  ADD_CONTAINER_FAILURE
} from '../actions/ActionTypes';
import {push} from 'react-router-redux';

const fetchContainersRequest = createAction(
  FETCH_CONTAINERS_REQUEST
);
const fetchContainersSuccess = createAction(
  FETCH_CONTAINERS_SUCCESS,
  containers => containers
);
const fetchContainersFailure = createAction(
  FETCH_CONTAINERS_FAILURE,
  ex=>ex.message
);

export function fetchContainers() {
  console.log("fetchContainers!!");
  return dispatch => {
    dispatch(fetchContainersRequest());
    return fetch(Config.apiHost + "/api/containers")
      .then(res => res.json())
      .then(json => dispatch(fetchContainersSuccess(json)))
      .catch(ex => dispatch(fetchContainersFailure(ex)))
  }
}

const addContainerRequest = createAction(
  ADD_CONTAINER_REQUEST
);
const addContainerSuccess = createAction(
  ADD_CONTAINER_SUCCESS,
  container => container
);
const addContainerFailure = createAction(
  ADD_CONTAINER_FAILURE,
  ex=>ex.message
);

export function addContainer(id, name, description) {
  console.log("addContainer!!", id, name, description);
  return dispatch => {
    dispatch(addContainerRequest());
    return fetch(Config.apiHost + "/api/containers", {
      method: "POST",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        id: id,
        name: name,
        description: description || undefined
      })
    })
      .then(res => res.json())
      .then(json => dispatch(addContainerSuccess(json)))
      .catch(ex => {
        console.log("failed to add container", ex);
        dispatch(addContainerFailure(ex))
      })
  }
}

export function navigateToViewContainer(id) {
  return dispatch => {
    dispatch(push("/containers/" + id));
  }
}
