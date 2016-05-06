import {combineReducers} from 'redux';
import {routerReducer} from "react-router-redux";
import AppReducer from "./AppReducer";
import ContainerReducer from "./ContainerReducer";
import ArchiveReducer from "./ArchiveReducer";
import LogFileReducer from "./LogFileReducer";

const rootReducer = combineReducers({
  AppReducer,
  ContainerReducer,
  ArchiveReducer,
  LogFileReducer,
  routing: routerReducer
});

export default rootReducer;
