import styles from './style.module.css'
import { Rating } from '@mui/material';
import Counter from '@components/Counter/Counter'
import BookingCalendar from '@components/bookingCalendar/BookingCalendar';

import { useContext, useState } from 'react'
// import { Context } from '../../App';
import { useSelector } from "react-redux";
import axios from 'axios';
import { useNavigate } from 'react-router-dom';





const ConfirmReservationScreen = () => {
    const reservation = useSelector((state) => state.reservation)
    const user = useSelector((state) => state.authentication)
    const [tourist, setTourist] = useState(reservation.tourist)
    const [date, setDate] = useState(reservation.date)
    // const [userLoggedEmail, setUserLoggedEmail] = useContext(Context)
    const [showModal, setShowModal] = useState(false)
    const [tour, setTour] = useState(reservation.tour)

    const navigate = useNavigate();



    const numberChange = (number) => {
        setTourist(number)
    }

    const handleDate = (e) => {
        setDate(e.target.value)
    }

    const closeModal = () => {
        navigate('/')
    }
    
    const reservationF = async () => {

        const reserve = {
            user: {email: user.user.email},
            bookingLineDTOS:[
                {
                    quantity: tourist,
                    date: date,
                    tourDTO: {
                        id: reservation.idTour
                    }
                }
            ],
            totalprice: null
        }

        console.log({user});
        console.log({reserve});
        const result = await axios.post(`/api/v1/digitalbooking/booking`, JSON.stringify(reserve),{
            headers:{
                'Content-Type' : 'application/json'
            }
        }).catch((error) => {
            console.log(error);
            console.log(error.response.data)
        })
        console.log(result);
        setShowModal(true)
    }

    
    return (
        <div className={styles.containerP}>
            <h1 className={styles.titleConfirm}>Your reservation is almost done</h1>
            <div className={styles.containerSections}>
                <section className={styles.sectionsConfirm1}>
                    <h2> Confirm your experience </h2>
                    <div className={styles.dataContainer}>
                        <div className={styles.data}>
                            <div>
                                <BookingCalendar className={styles.confirmData} id={reservation.idTour} setDate={setDate} date={date} tour={tour}/>
                                {/* <input id='date' type='date' className={styles.styledInput} value={date} onChange={handleDate}/> */}
                            </div>
                        </div>
                        <div className={styles.data}>
                            <p> Tourist </p>
                            <Counter number={tourist} numberChange={numberChange}/>
                        </div>
                    </div>
                    <div className={styles.containerButton}>
                        <button className={styles.button} onClick={reservationF}> Reserve </button>
                    </div>
                </section>
                <section className={styles.sectionsConfirm2}>
                        <img src={reservation.imageTour}/>
                        <p>{reservation.nameTour}</p>
                        <Rating
                            max={5}
                            precision={0.5}
                            value={4}
                        />
                </section>
            </div>
            {showModal && 
            <div id="myModal" className={styles.reserveModal}>
                <div className={styles.modalContent}>
                    <i className="bi bi-check-circle"/>
                    <h2>Thank you </h2>
                    <p> Your reserve was success</p>
                    <button className={styles.button} onClick={closeModal}>ok</button>
                </div>
            </div>
            }
        </div>
    )
}




export default ConfirmReservationScreen
