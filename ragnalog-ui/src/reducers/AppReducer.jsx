import {handleActions} from 'redux-actions';
import {
  FETCH_LOGTYPES_REQUEST,
  FETCH_LOGTYPES_SUCCESS,
  FETCH_LOGTYPES_FAILURE,
  CHANGE_TITLE,
  ADD_MESSAGE,
  READ_MESSAGE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_LOGTYPES_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_LOGTYPES_SUCCESS]: (state, action) => ({
    ...state,
    logTypes: action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [FETCH_LOGTYPES_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [CHANGE_TITLE]: (state, action) => ({
    ...state,
    title: action.payload
  }),
  [ADD_MESSAGE]: (state, action) => ({
    ...state,
    messageIdCounter: state.messageIdCounter + 1,
    messages: [{
      id: state.messageIdCounter,
      message: action.payload,
      unread: true,
      date: new Date()
    }, ...state.messages]
  }),
  [READ_MESSAGE]: (state, action) => {
    const index = state.messages.findIndex(m => m.id === action.payload);
    const msg = state.messages[index];
    return ({
      ...state,
      messages: [
        ...state.messages.slice(0, index),
        {
          ...msg,
          unread: false
        },
        ...state.messages.slice(index + 1)
      ]
    });
  }
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  title: "Ragnalog",
  logTypes: [],
  containerNames: [],
  messages: [],
  messageIdCounter: 0
});
