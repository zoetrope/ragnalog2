import * as types from '../constants/ActionTypes';

export function addContainer(id, name, description) {
  return {type: types.ADD_CONTAINER, name, description};
}

export function deleteContainer(id) {
  return {type: types.DELETE_CONTAINER, id};
}

export function editContainer(id, name, description) {
  return {type: types.EDIT_CONTAINER, id, name, description};
}

