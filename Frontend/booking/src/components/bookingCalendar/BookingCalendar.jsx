import React from "react";

import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

/* Prop Types */
import PropTypes from 'prop-types'

/* Styles */
import styles from './style.module.css'

const BookingCalendar = ({setDate, date, tour }) => {
    const tourAvailability = []
    const disabledDates = [];

    function getKeys (params) {
        for (const key in params) {
            if (Object.hasOwnProperty.call(params, key)) {
                const element = params[key];
                if(element > 0){
                    tourAvailability.push({date: key, availability: element})
                }
            }
        }
        tourAvailability.map(tourAvailable => disabledDates.push({date : new Date(tourAvailable.date), availability : tourAvailable.availability}))
    }
    /* Fix availability */
    getKeys(tour.availability)

    const isDateDisabled = date => {
        return disabledDates.some(disabledDate =>
            isSameDay(disabledDate.date, date)
            );
        };

        const isSameDay = (date1, date2) => {
        return (
            date1.getUTCFullYear() === date2.getUTCFullYear() &&
            date1.getUTCMonth() === date2.getUTCMonth() &&
            date1.getUTCDate() === date2.getUTCDate()
        );
    };

    return (
        <div className={styles.calendar}>
            <label htmlFor="datepicker">Select a date:</label>
            <DatePicker
                showIcon={true}
                id="datepicker"
                selected={date}
                onChange={(date) => setDate(date)}
                filterDate={(date) => isDateDisabled(date)}
            />
        </div>
    );
};

BookingCalendar.propTypes = {
    id: PropTypes.number,
    setDate : PropTypes.func,
    date: PropTypes.string,
    tour : PropTypes.object,
}

export default BookingCalendar
