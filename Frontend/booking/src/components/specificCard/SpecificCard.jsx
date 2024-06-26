import React from 'react'

/* Prop Types */
import PropTypes from 'prop-types'

/* Styles */
import styles from "./style.module.css";

const SpecificCard = ({submitResult}) => {
    console.log(submitResult);

    return (
        <div className={styles.specificCard}>
            <img src={submitResult.images.find(image => image.isPrincipal).urlImage} alt={submitResult.name} />
            <hr />
            <div className={styles.dataContainer}>
                <h2>{submitResult.name}</h2>
                <p className={styles.dataDescription}>{submitResult.description}</p>
            </div>
        </div>
    )
}

SpecificCard.propTypes = {
    submitResult: PropTypes.object,
}

export default SpecificCard