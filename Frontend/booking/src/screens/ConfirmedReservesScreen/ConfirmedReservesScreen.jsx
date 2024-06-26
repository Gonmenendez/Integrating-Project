import React, { useEffect, useState } from 'react'

import {useSelector} from "react-redux";
/* Axios */
import axios from 'axios'

/* Styles */
import styles from "./style.module.css";


function ConfirmedReservesScreen() {

  const [reservations, setReservations] = useState([]);
  const user = useSelector((state) => state.authentication.user);
  if(!user) window.location.replace("/loginUser");

  useEffect(() => {
    try {
      const apiRequest = async () => {
        const result = await axios(
          `/api/v1/digitalbooking/booking/user/${user.email}`
        ).catch((error) => {
          console.log(error);
          console.log(error.response.data);
        });
        console.log(result.data);
        setReservations(result.data);
      };
      apiRequest();
    } catch (error) {
      console.log("Hubo error: " + error);
    }
  }, [user]);


  return (
    <>
      <section className={styles.reservesContainer}>
        <h1>Confirmed Reservations</h1>
        {reservations.length > 0 && (
          <>
            {reservations[0].bookingLineDTOS.map((reservation) => (
              <article key={reservation.id} className={styles.sectionContainer}>
                <div className={styles.reserve}>
                  <div className={styles.info}>
                    <span>{reservation.tourDTO.name} </span>
                    <span>
                      <span>{reservation.quantity}</span>
                      <span>pers</span>
                    </span>
                    <span>${reservation.tourDTO.price}</span>
                    <span>{reservation.date}</span>
                  </div>
                  {reservation.tourDTO.images.length > 0 && <img src={reservation.tourDTO.images.find((image) => image.isPrincipal).urlImage} alt={reservation.tourDTO.name}/>}
                </div>
                <hr />
              </article>))}
            {/* <p>Total Price ${reservations.totalPrice}</p> */}
          </>
        )}
        {reservations.length === 0 && (
          <div className='error'>This user has no reservations</div>
        )}
      </section>
    </>
  );
}

export default ConfirmedReservesScreen