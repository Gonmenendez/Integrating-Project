import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  date: 1,
  tourist: 1,
  idTour: 1,
  nameTour: '',
  imageTour: '',
  tour : {}
};

export const reservationSlice = createSlice({
  name: "reservation",
  initialState,
  reducers: {
    setReservation: (state, action) => {
        state.date = action.payload.date;
        state.tourist = action.payload.tourist;
        state.idTour = action.payload.idTour;
        state.nameTour = action.payload.nameTour;
        state.imageTour = action.payload.imageTour;
        state.tour = action.payload.tour
    },
  },
});

// Action creators are generated for each case reducer function
export const {setReservation} = reservationSlice.actions;

export default reservationSlice.reducer;
