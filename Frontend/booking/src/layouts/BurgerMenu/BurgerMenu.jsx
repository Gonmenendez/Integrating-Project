import React, { useState } from 'react'

/* React Router */
import { Link } from 'react-router-dom';

/* Routes */
import { ROUTES } from '@utils/ResourceRoutes';

/* Styles */
import styles from "./style.module.css";

/* Redux hooks */
import { useDispatch, useSelector } from "react-redux";
import { setIsLoggedIn, setUser } from "../../redux/authSlice";

const BurgerMenu = () => {
  const [navVisible, setNavVisible] = useState(false)

  const dispatch = useDispatch();
  const toggleNavVisibility = () => {
    setNavVisible(!navVisible)
  }

    const isLoggedIn = useSelector((state) => state.authentication.isLoggedIn);
    const user = useSelector((state) => state.authentication.user);
    console.log("Usuario logeado: ", isLoggedIn);

    const logout = (e) => {
      if(confirm("Are you sure you want to logout?")){
        toggleNavVisibility;
        dispatch(setIsLoggedIn(false));
        dispatch(setUser({}));
      }else{
        e.preventDefault();
      }
    }

    const getUserInitials = (user) =>{
      if(user.firstName && user.lastName){
        return `${user.firstName.charAt(0)}${user.lastName.charAt(0)}`;
      }
      return "";
    }

    return (
      <div>
        <div className={styles.burgerMenu}>
          <button className={styles.openMenu} onClick={toggleNavVisibility}>
            <i className="bi bi-list"></i>
          </button>
          <nav
            className={`${styles.navBar} ${navVisible ? styles.visible : ""}`}
          >
            <button className={styles.closeMenu} onClick={toggleNavVisibility}>
              <i className="bi bi-x"></i>
            </button>
            <h3>Menu</h3>
            <ul className={styles.separator}>
              {!isLoggedIn && (
                <>
                  <li>
                    <Link to={ROUTES.loginUser} onClick={toggleNavVisibility}>
                      Sign in
                    </Link>
                  </li>
                  <li>
                    <Link
                      to={ROUTES.registerUser}
                      onClick={toggleNavVisibility}
                    >
                      Sign up
                    </Link>
                  </li>
                </>
              )}
              {isLoggedIn && (
                <>
                  <li>{`Welcome ${user.firstName}`}</li>
                  <li>
                    <Link to={ROUTES.confirmedReserves}>
                      My Reserves
                    </Link>
                  </li>
                  <li>
                    <Link to={ROUTES.home} onClick={(e) => logout(e)}>
                      Logout
                    </Link>
                  </li>
                </>
              )}
            </ul>
          </nav>
        </div>
        <nav className={styles.desktopNav}>
          <ul>
            {!isLoggedIn && (
              <>
                <li className={styles.signIn}>
                  <Link to={ROUTES.loginUser}>Sign in</Link>
                </li>
                <li className={styles.signUp}>
                  <Link to={ROUTES.registerUser}>Sign up</Link>
                </li>
              </>
            )}
            {isLoggedIn && (
              <>
                <li>
                  <div className={styles.avatar}>
                    {getUserInitials(user)}
                  </div>
                </li>
                <li className={styles.signUp}>
                  <Link to={ROUTES.confirmedReserves}>
                    My Reserves
                  </Link>
                </li>
                <li className={styles.signUp}>
                  <Link to={ROUTES.home} onClick={(e) => logout(e)}>
                    Logout
                  </Link>
                </li>
              </>
            )}
          </ul>
        </nav>
      </div>
    );
}

export default BurgerMenu