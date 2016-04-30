import {
  FETCH_CONTAINERS_REQUEST,
  FETCH_CONTAINERS_SUCCESS,
  FETCH_CONTAINERS_FAILURE
} from './ActionTypes';

import {createAction} from 'redux-actions';

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
    return fetch("http://localhost:8686/api/containers")
      .then(res => res.json())
      .then(json => dispatch(fetchContainersSuccess(json)))
      .catch(ex => dispatch(fetchContainersFailure(ex)))
  }
}

