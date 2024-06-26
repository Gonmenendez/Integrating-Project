import React from 'react'

/* Styles */
import styles from './style.module.css'

/* Components */
import Categories from '@components/Categories/Categories'
import Search from "@components/Search/Search";
import Recommended from "@components/Recommended/Recommended";

import {Outlet} from "react-router-dom";

const Home = () => {
    return (
        <>
            <div className={styles.body}>
                <Search/>
                <Recommended/>
                <Categories/>
            </div>
            <Outlet/>
        </>
    )
}

export default Home
