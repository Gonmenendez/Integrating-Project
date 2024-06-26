import React, { useEffect, useRef } from 'react'

/* Styles */
import styles from './style.module.css'

/* Prop Types */
import PropTypes from 'prop-types'

/* Components */
import AddProductCalendar from '@components/addProductCalendar/AddProductCalendar'

const NewTourDates = ({setCalendarDisplayed, setTourDates, tourDates, tourDatesController}) => {
    const menuRef = useRef()

    useEffect(() => {
        let outClickHandler = (e) =>{
            if(!menuRef.current.contains(e.target)){
                e.preventDefault()
                setCalendarDisplayed(false)
                tourDatesController()
            }
        }
        document.addEventListener('mousedown', outClickHandler)

        return() => {
            document.removeEventListener('mousedown', outClickHandler)
        }
    })

    return (
        <div className={styles.datePicker}>
            <div className={styles.datePickerContainer} ref={menuRef}>
                <AddProductCalendar tourDates={tourDates} setTourDates={setTourDates}/>
                <ul>
                    {tourDates?.map(date => (
                        <li key={date.toString()}>{date.toLocaleDateString()}</li>
                    ))}
                </ul>
            </div>
        </div>
    )
}

NewTourDates.propTypes = {
    setCalendarDisplayed : PropTypes.func,
    setTourDates : PropTypes.func,
    tourDates : PropTypes.array,
    tourDatesController : PropTypes.func
}

export default NewTourDates
