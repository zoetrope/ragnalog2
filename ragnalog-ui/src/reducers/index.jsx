import {combineReducers} from 'redux';
import {routerReducer} from "react-router-redux";
import ContainerReducers from "./ContainerReducer";

const rootReducer = combineReducers({
  ContainerReducers,
  routing: routerReducer
});

export default rootReducer;
