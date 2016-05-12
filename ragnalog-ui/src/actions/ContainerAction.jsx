import {createAction} from "redux-actions";
import * as Config from "../store/Configuration";
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
} from './ActionTypes';
import {push} from 'react-router-redux';

const fetchContainersRequest = createAction(
  FETCH_CONTAINERS_REQUEST,
  status => status
);
const fetchContainersSuccess = createAction(
  FETCH_CONTAINERS_SUCCESS,
  containers => containers
);
const fetchContainersFailure = createAction(
  FETCH_CONTAINERS_FAILURE,
  ex => ex.message
);

export function fetchContainers(status) {
  console.log("fetchContainers!!", status);
  return dispatch => {
    dispatch(fetchContainersRequest(status));
    return fetch(Config.apiHost + "/api/containers?status=" + status)
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
  ex => ex.message
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

const updateContainerRequest = createAction(
  UPDATE_CONTAINER_REQUEST
);
const updateContainerSuccess = createAction(
  UPDATE_CONTAINER_SUCCESS,
  container => container
);
const updateContainerFailure = createAction(
  UPDATE_CONTAINER_FAILURE,
  ex => ex.message
);

export function updateContainer(id, name, description) {
  console.log("updateContainer!!", id, name, description);
  return dispatch => {
    dispatch(updateContainerRequest());
    return fetch(Config.apiHost + "/api/containers/" + id, {
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: name,
        description: description || undefined
      })
    })
      .then(res => res.json())
      .then(json => dispatch(updateContainerSuccess(json)))
      .catch(ex => {
        console.log("failed to update container", ex);
        dispatch(updateContainerFailure(ex))
      })
  }
}

const changeContainerStatusRequest = createAction(
  CHANGE_CONTAINERSTATUS_REQUEST,
  status => status
);
const changeContainerStatusSuccess = createAction(
  CHANGE_CONTAINERSTATUS_SUCCESS,
  container => container
);
const changeContainerStatusFailure = createAction(
  CHANGE_CONTAINERSTATUS_FAILURE,
  ex => ex.message
);

export function changeContainerStatus(id, status) {
  console.log("changeContainerStatus!!");
  return dispatch => {
    dispatch(changeContainerStatusRequest(status));
    return fetch(Config.apiHost + "/api/containers/" + id, {
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        status: status
      })
    })
      .then(res => res.json())
      .then(json => dispatch(changeContainerStatusSuccess(json)))
      .catch(ex => {
        console.log("failed to activate container", ex);
        dispatch(changeContainerStatusFailure(ex))
      })
  }
}

export function changeStatus(status) {
  return dispatch => {
    dispatch(push("/containers?status=" + status));
  }
}

export function navigateToViewContainer(id) {
  return dispatch => {
    dispatch(push("/containers/" + id));
  }
}
