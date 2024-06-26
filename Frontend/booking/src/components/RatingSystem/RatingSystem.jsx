import React, { useState } from 'react'
import { Rating } from '@mui/material';
/* Axios */
import axios from 'axios'

/* Styles */
import styles from './style.module.css'
import { useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

function RatingSystem() {

    const [userValue, setUserValue] = useState(0)
    const [newAverage, setNewAverage] = useState(null)
    const isLoggedIn = useSelector((state) => state.authentication.isLoggedIn);
    const userLoggedEmail = useSelector((state) => state.authentication.user.email);
    const { id } = useParams();
    const handleChange = (event, newValue) => {
        setUserValue(newValue);
        console.log('email loggeado: ', userLoggedEmail);
        console.log('valor registrado: ', newValue);


        //envio a la api con el incremento
        try {

            const userValueInput = {
                rating: newValue,
                mail: userLoggedEmail,
            }

            const apiRatingAdd = async () => {
                const resultAverage = await axios.post(`/api/v1/digitalbooking/tour/rating/${id}`, userValueInput,
                    {
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    });

                setNewAverage(resultAverage.data);

                alert('Rating successfully updated')

                console.log('New average is: ', newAverage);
                console.log('Rated product id is: ', id);
            }

            apiRatingAdd()

        } catch (error) {
            console.log('Hubo error: ' + error);

        }

    }

    return (
      <div className={styles.container}>
        <Rating
          max={5}
          precision={0.5}
          value={userValue}
          onChange={handleChange}
          {...(!isLoggedIn && {disabled: true})}
        />

        <span className={styles.ratingValue}>{newAverage}</span>
      </div>
    );
}

export default RatingSystem