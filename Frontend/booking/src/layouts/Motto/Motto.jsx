// eslint-disable-next-line no-unused-vars
import React from 'react'

/* React Router */
import { Link, Outlet } from 'react-router-dom'

/* Routes */
import { ROUTES } from "@utils/ResourceRoutes";

/* Assets */
import logo from "@assets/logo-dh.png"

/* Styles */
import styles from "./style.module.css";


function Motto() {
    return (
        <>
            <div className={styles.brand}>
                <Link to={ROUTES.home}>
                    <img src={logo} alt="DH Tours" />
                </Link>
                <Link to={ROUTES.home} className={styles.mottoLink}>
                    <p className={styles.motto}>Come travel with us!</p>
                </Link>
            </div>
        <Outlet/>
        </>
    )
}



export default Motto