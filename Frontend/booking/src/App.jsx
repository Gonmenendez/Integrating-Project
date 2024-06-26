
/* Styles */
import "./App.css";

/* React Router */
import {BrowserRouter, Route, Routes} from "react-router-dom";

import React from "react";

/* Layout components */
import Header from "@layouts/Header/Header";
import Footer from "@layouts/Footer/Footer";

/* Routes */
import {PUBLIC_ROUTES} from "@utils/ResourceRoutes";


function App() {

  return (
    <div className="full">
      <BrowserRouter>
          <Header />
          <Routes>
            {PUBLIC_ROUTES.map((route) => {
              return (
                <Route
                  key={route.path}
                  path={route.path}
                  element={<route.element />}
                />
              );
            })}
          </Routes>
          <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
