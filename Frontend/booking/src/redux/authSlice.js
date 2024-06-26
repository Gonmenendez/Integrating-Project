import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  user: { name: '', email: '', id:'' },
  isLoggedIn: false,
};

export const authSlice = createSlice({
  name: "authentication",
  initialState,
  reducers: {
    setUser: (state, action) => {
        state.user = action.payload;
    },
    setIsLoggedIn: (state, action) => {
        state.isLoggedIn = action.payload;
    }
  },
});

// Action creators are generated for each case reducer function
export const {setUser, setIsLoggedIn} = authSlice.actions;

export default authSlice.reducer;
