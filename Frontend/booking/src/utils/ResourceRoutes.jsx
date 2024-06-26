/*Read only object, contain all routes for navigate in UI by clients*/

/* Screen elements */
import HomeScreen from "@screens/HomeScreen/HomeScreen";
import ProductFormScreen from "@screens/ProductFormScreen/ProductFormScreen";
import SignInScreen from "@screens/SignInScreen/SignInScreen";
import SignUpScreen from "@screens/SignUpScreen/SignUpScreen";
import ToursScreen from "@screens/ToursScreen/ToursScreen";
import AddCategoryFormScreen from "@screens/AddCategoryFormScreen/AddCategoryFormScreen";
import ConfirmReservationScreen from "@screens/ConfirmReservationScreen/ConfirmReservationScreen";
import ConfirmedReservesScreen from "@screens/ConfirmedReservesScreen/ConfirmedReservesScreen";
import Pagination from "@/screens/pagination/Pagination";

export const PUBLIC_ROUTES = [
  {name: "home", path: "/", element: HomeScreen},
  {name: "toursById", path: "/tours/:id", element: ToursScreen},
  {name: "addProduct", path: "/addProduct", element: ProductFormScreen},
  {name: "registerUser", path: "/registerUser", element: SignUpScreen},
  {name: "loginUser", path: "/loginUser", element: SignInScreen},
  {name: "addCategory", path: "/addCategory", element: AddCategoryFormScreen},
  {name: "confirmReserve", path: "/confirmReserve", element: ConfirmReservationScreen},
  {name: "confirmedReserves", path: "/confirmedReserves", element: ConfirmedReservesScreen},
  {name: "pagination", path: "/pagination", element: Pagination}
];

export const ROUTES = new Proxy(PUBLIC_ROUTES, {
  get: function (obj, prop) {
    const rutaEncontrada = obj.find((ruta) => ruta.name === prop);
    return rutaEncontrada ? rutaEncontrada.path : undefined;
  },
});

