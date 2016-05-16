import {handleActions} from 'redux-actions';
import {
  FETCH_ARCHIVES_REQUEST,
  FETCH_ARCHIVES_SUCCESS,
  FETCH_ARCHIVES_FAILURE,
  ADD_ARCHIVE_REQUEST,
  ADD_ARCHIVE_SUCCESS,
  ADD_ARCHIVE_FAILURE,
  DELETE_ARCHIVE_REQUEST,
  DELETE_ARCHIVE_SUCCESS,
  DELETE_ARCHIVE_FAILURE
} from '../actions/ActionTypes';

export default handleActions({
  [FETCH_ARCHIVES_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [FETCH_ARCHIVES_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [FETCH_ARCHIVES_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [ADD_ARCHIVE_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [ADD_ARCHIVE_SUCCESS]: (state, action) => ({
    ...action.payload,
    isFetching: false,
    error: false,
    errorMessage: ""
  }),
  [ADD_ARCHIVE_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  }),
  [DELETE_ARCHIVE_REQUEST]: state => ({
    ...state,
    isFetching: true,
    error: false,
    errorMessage: ""
  }),
  [DELETE_ARCHIVE_SUCCESS]: (state, action) => {
    const index = state.archives.findIndex(archive => archive.id === action.payload.id);
    return ({
      ...state,
      archives: [
        ...state.archives.slice(0, index),
        ...state.archives.slice(index + 1)
      ],
      isFetching: false,
      error: false,
      errorMessage: ""
    });
  },
  [DELETE_ARCHIVE_FAILURE]: (state, action) => ({
    ...state,
    isFetching: false,
    error: true,
    errorMessage: action.payload
  })
}, {
  isFetching: false,
  error: false,
  errorMessage: "",
  archives: []
});
