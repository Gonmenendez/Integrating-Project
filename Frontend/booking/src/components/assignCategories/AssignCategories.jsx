import React, { useEffect, useRef, useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Prop Types */
import PropTypes from 'prop-types'
import LoadingAnimated from '../../layouts/LoadingAnimated/LoadingAnimated'

const AssignCategories = ({categories, setCategoriesDisplayed, tourData, setTourData, categoriesLoaded}) => {
    const checkboxes = document.getElementsByName('category');
    useEffect(()=>{
        let categories = []
        checkboxes.forEach(checkbox => {
            checkbox.addEventListener('click', ()=>{
                if(checkbox.checked){
                    tourData.categories.find(category => category.title == checkbox.value) ? '' : categories.push({title: checkbox.value})
                }
            })
        });

        setTourData({
            ...tourData,
            categories: categories
        })
    },[categories])


    const menuRef = useRef()

    useEffect(() => {
        let outClickHandler = (e) =>{
            if(!menuRef.current.contains(e.target)){
                e.preventDefault()
                setCategoriesDisplayed(false)
            }
        }
        document.addEventListener('mousedown', outClickHandler)

        return() => {
            document.removeEventListener('mousedown', outClickHandler)
        }
    })

    return (
        <>
        {categoriesLoaded ?
            <div className={styles.assignCategories}>
                <div className={styles.categoriesContainer} ref={menuRef}>
                    <h1>Categories</h1>
                    <form>
                        {categories.map(category => 
                            <label htmlFor={category.title} key={category.id}><input type="checkbox" name='category' value={category.title} id={category.title}/>{category.title}</label>
                            )}
                    </form>
                    <button className={styles.categoriesButton} onClick={() => setCategoriesDisplayed(false)}>Pick category</button>
                </div>
            </div>
            : 
            <div className={styles.assignCategories}>
                <div className={styles.categoriesContainer} ref={menuRef}>
                    <p>Loading...</p>
                    <LoadingAnimated/>
                    <p>(Please wait, this might take a while)</p>
                </div>
            </div>
        }   
        </>
    )
}

AssignCategories.propTypes = {
    categories : PropTypes.array,
    setCategoriesDisplayed : PropTypes.func,
    tourData : PropTypes.object,
    setTourData : PropTypes.func,
    categoriesLoaded : PropTypes.bool
}

export default AssignCategories