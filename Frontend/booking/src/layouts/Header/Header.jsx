import React from 'react'

/* Styles */
import styles from "./style.module.css"

/* Components */
import BurgerMenu from '@layouts/BurgerMenu/BurgerMenu.jsx';
import Motto from "@layouts/Motto/Motto";

const Header = () => { 
  return (
    <div>
      <div className={styles.header}>
        <Motto />
        <BurgerMenu/>
      </div>
      <div className={styles.space}/>
    </div>
  )
}

export default Header

