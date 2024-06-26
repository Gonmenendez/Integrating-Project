import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'

/* Styles */
import "./index.css";

/* Redux */
import {store} from "./redux/store";
import {Provider} from "react-redux";
import {persistStore} from "redux-persist";
import {PersistGate} from "redux-persist/integration/react";

let persistor = persistStore(store);


ReactDOM.createRoot(document.getElementById("root")).render(
  <Provider store={store}>
    <PersistGate persistor={persistor}>
      <App />
    </PersistGate>
  </Provider>
);