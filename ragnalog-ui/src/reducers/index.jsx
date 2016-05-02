import {combineReducers} from 'redux';
import {routerReducer} from "react-router-redux";
import ContainerReducer from "./ContainerReducer";
import ArchiveReducer from "./ArchiveReducer";

const rootReducer = combineReducers({
  ContainerReducer,
  ArchiveReducer,
  routing: routerReducer
});

export default rootReducer;
