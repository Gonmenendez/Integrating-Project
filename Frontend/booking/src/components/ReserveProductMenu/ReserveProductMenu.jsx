import React, { useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Routes */
import { ROUTES } from '@utils/ResourceRoutes';
import { Link} from 'react-router-dom'

// Context
// import { Context } from '../../App'
import Counter from '@components/Counter/Counter';
import { useDispatch } from "react-redux";
import { setReservation } from '../../redux/reservationSlice';

/* Navigation */
import { useNavigate } from "react-router-dom";
import BookingCalendar from '../bookingCalendar/BookingCalendar';
import { useSelector } from 'react-redux';

/* Prop Types */
import PropTypes from 'prop-types'

function ReserveProductMenu({id, availability, tour, nameTour, imageTour}) {

  const isLoggedIn = useSelector((state) => state.authentication.isLoggedIn);
  const [cantTourist, setCantTourist] = useState(1)
  const history = useNavigate();
  const [date, setDate] = useState(null)
  const dispatch = useDispatch();


  function handleSubmit(e) {
    e.preventDefault()
    if(date){
      dispatch(setReservation({
        date: date,
        tourist: cantTourist,
        nameTour: nameTour,
        imageTour: imageTour.urlImage,
        idTour: id,
        tour: tour
      }))
      history('/confirmReserve')
    }
  }

  const numberChange = (number) => {
      setCantTourist(number)
  }

  return (
    <>
      <form className={styles.container} onSubmit={handleSubmit}>
        {tour.availability && (
          <>
            <article className={styles.calendarContainer}>
              <BookingCalendar
                id={id}
                setDate={setDate}
                date={date}
                tour={tour}
              />
              <div className={styles.tourists}>
                <p> Tourists </p>
                <Counter number={cantTourist} numberChange={numberChange} />
              </div>
            </article>

            {isLoggedIn ? (
              <button
                type="submit"
                onClick={handleSubmit}
                className={styles.btnSubmitReserve}
              >
                Reserve Now!
              </button>
            ) : (
              <Link to={ROUTES.loginUser} className={styles.links}>
                <button className={styles.btnSubmitReserve}>
                  Reserve Now!
                </button>
              </Link>
            )}
          </>
        )}
        {!tour.availability && (
          <>This tour is not available at this moment</>
        )}
      </form>
    </>
  );
}

ReserveProductMenu.propTypes = {
  id: PropTypes.number,
  availability : PropTypes.object,
  tour : PropTypes.object,
  nameTour : PropTypes.string,
  imageTour : PropTypes.object
}

export default ReserveProductMenu;