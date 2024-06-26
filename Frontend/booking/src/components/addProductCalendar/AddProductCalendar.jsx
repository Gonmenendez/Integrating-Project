import React, { useState } from 'react';

import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';

/* Prop Types */
import PropTypes from 'prop-types'


const AddProductCalendar = ({tourDates, setTourDates}) => {    
    const [datesString, setDatesString] = useState([])

    const handleDateChange = date => {
        if(!datesString.includes(date.toLocaleDateString())){
            setTourDates([...tourDates, date])
            setDatesString([...datesString, date.toLocaleDateString()])
        }
    };
    
    return (
        <DatePicker
        selected={null}
        onChange={handleDateChange}
        inline
        multiple
        />
    );
}

AddProductCalendar.propTypes = {
    setTourDates : PropTypes.func,
    tourDates : PropTypes.array
}

export default AddProductCalendar
