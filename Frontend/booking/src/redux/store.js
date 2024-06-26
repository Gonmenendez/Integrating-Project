import {configureStore} from "@reduxjs/toolkit";
import authSlice from "./authSlice";
import {combineReducers} from "redux";
import {persistReducer} from "redux-persist";
import storage from "redux-persist/lib/storage";
import reservationSlice from "./reservationSlice";


const persistConfig = {
  key: "root",
  storage,
};

const rootReducer = combineReducers({
  authentication: authSlice, 
  reservation: reservationSlice, 
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
   middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});
