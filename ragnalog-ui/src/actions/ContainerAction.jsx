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
  ACTIVATE_CONTAINER_REQUEST,
  ACTIVATE_CONTAINER_SUCCESS,
  ACTIVATE_CONTAINER_FAILURE,
  DEACTIVATE_CONTAINER_REQUEST,
  DEACTIVATE_CONTAINER_SUCCESS,
  DEACTIVATE_CONTAINER_FAILURE
} from './ActionTypes';
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

const updateContainerRequest = createAction(
  UPDATE_CONTAINER_REQUEST
);
const updateContainerSuccess = createAction(
  UPDATE_CONTAINER_SUCCESS,
  container => container
);
const updateContainerFailure = createAction(
  UPDATE_CONTAINER_FAILURE,
  ex=>ex.message
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

const activateContainerRequest = createAction(
  ACTIVATE_CONTAINER_REQUEST
);
const activateContainerSuccess = createAction(
  ACTIVATE_CONTAINER_SUCCESS,
  container => container
);
const activateContainerFailure = createAction(
  ACTIVATE_CONTAINER_FAILURE,
  ex=>ex.message
);

export function activateContainer(id) {
  console.log("activateContainer!!");
  return dispatch => {
    dispatch(activateContainerRequest());
    return fetch(Config.apiHost + "/api/containers/" + id, {
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        status: "active"
      })
    })
      .then(res => res.json())
      .then(json => dispatch(activateContainerSuccess(json)))
      .catch(ex => {
        console.log("failed to activate container", ex);
        dispatch(activateContainerFailure(ex))
      })
  }
}

const deactivateContainerRequest = createAction(
  DEACTIVATE_CONTAINER_REQUEST
);
const deactivateContainerSuccess = createAction(
  DEACTIVATE_CONTAINER_SUCCESS,
  container => container
);
const deactivateContainerFailure = createAction(
  DEACTIVATE_CONTAINER_FAILURE,
  ex=>ex.message
);

export function deactivateContainer(id) {
  console.log("deactivateContainer!!", id);
  return dispatch => {
    dispatch(deactivateContainerRequest());
    return fetch(Config.apiHost + "/api/containers/" + id, {
      method: "PUT",
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        status: "inactive"
      })
    })
      .then(res => res.json())
      .then(json => dispatch(deactivateContainerSuccess(json)))
      .catch(ex => {
        console.log("failed to deactivate container", ex);
        dispatch(deactivateContainerFailure(ex))
      })
  }
}

export function navigateToViewContainer(id) {
  return dispatch => {
    dispatch(push("/containers/" + id));
  }
}
