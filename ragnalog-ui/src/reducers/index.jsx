import {combineReducers} from 'redux';
import {routerReducer} from "react-router-redux";
import ContainerReducer from "./ContainerReducer";
import ArchiveReducer from "./ArchiveReducer";
import LogFileReducer from "./LogFileReducer";

const rootReducer = combineReducers({
  ContainerReducer,
  ArchiveReducer,
  LogFileReducer,
  routing: routerReducer
});

export default rootReducer;
