// eslint-disable-next-line no-unused-vars
import React from 'react'

/* Styles */
import styles from "./style.module.css"

/* Logo */
import logo from "@assets/logo-dh.png";


const Footer = () => {
  return (
    <div className={styles.footer}>
      <img src={logo} alt="" />
      <div className="copy">
        <p>&copy; {new Date().getFullYear()} - DH Tours - Equipo 3 - Proyecto Integrador</p>
      </div>
    </div>
  );
}

export default Footer