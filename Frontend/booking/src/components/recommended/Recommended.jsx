import React, { useEffect, useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Components */
import Cards from "@components/Cards/Cards";

const Recommended = () => {
    return (
        <section>
            <div className={styles.recommendationsTitle}>
                <h2 className={styles.h2recommend}>Recommendations</h2>
                <hr className={styles.recommendHr}/>
            </div>
            <Cards/>
        </section>
    )
}

export default Recommended