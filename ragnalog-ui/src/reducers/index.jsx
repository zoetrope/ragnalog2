import {combineReducers} from 'redux';
import {routerReducer} from "react-router-redux";
import ContainerReducer from "./ContainerReducer";

const rootReducer = combineReducers({
  ContainerReducer,
  routing: routerReducer
});

export default rootReducer;
