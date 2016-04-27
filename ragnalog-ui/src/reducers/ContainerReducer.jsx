import {ADD_CONTAINER, DELETE_CONTAINER, EDIT_CONTAINER} from '../actions/ActionTypes';

const initialState = [{
  text: 'Use Redux',
  completed: false,
  id: 0
}];

export default function ContainerReducer(state = initialState, action) {
  switch (action.type) {
    case ADD_CONTAINER:
      return [{
        id: state.reduce((maxId, container) => Math.max(container.id, maxId), -1) + 1,
        name: action.name,
        description: action.description
      }, ...state];

    case DELETE_CONTAINER:
      return state.filter(container =>
        container.id !== action.id
      );

    case EDIT_CONTAINER:
      return state.map(container =>
        container.id === action.id ?
          Object.assign({}, container, {name: action.name, description: action.description}) :
          container
      );

    default:
      return state;
  }
}
