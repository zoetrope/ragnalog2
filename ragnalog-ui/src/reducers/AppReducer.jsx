import {handleActions} from 'redux-actions';
import {
  FETCH_LOGTYPES_REQUEST,
  FETCH_LOGTYPES_SUCCESS,
  FETCH_LOGTYPES_FAILURE,
  CHANGE_TITLE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_LOGTYPES_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_LOGTYPES_SUCCESS]: (state, action) => ({
    ...action.payload,
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
    title: action.payload
  })
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  title: "Ragnalog",
  logTypes: [],
  containerNames: []
});
