import React, { useState, useEffect, useRef } from 'react'

/* Axios */
import axios from 'axios'

/* Styles */
import styles from './style.module.css'

/* Prop Types */
import PropTypes from 'prop-types'

/* Components */
import LoadingAnimated from '@layouts/LoadingAnimated/LoadingAnimated';

function AddCategoryFormScreen({setAddCategoryDisplayed}) {
    //Error message and error handler for title
    const [titleError, setTitleError] = useState(true)
    const [titleMessage, setTitleMessage] = useState('')
    //Error message and error handler for description
    const [descriptionError, setDescriptionError] = useState(true)
    const [descriptionMessage, setDescriptionMessage] = useState('')
    //Error message and error handler for category image
    const [categoryImageError, setCategoryImageError] = useState(true)
    const [categoryImageMessage, setCategoryImageMessage] = useState('')
    //Handle submit
    const [sumbitDisabled, setSubmitDisabled] = useState(true)
    const [categoryData, setCategoryData] = useState({
        title: '' ,
        description: '' ,
    })
    const formData = new FormData();

    const [data, setData] = useState('[]')


    //Title validations
    function titleLimit(e){
        if(e.target.value.trim().length == 0){
            setTitleError(true)
            setTitleMessage("This field can't be empty")
        } else if (e.target.value.trim().length <= 4){
            setTitleError(true)
            setTitleMessage('Title must be at least 5 characters long')
        } else if (e.target.value.trim().length >= 20){
            setTitleError(true)
            setTitleMessage('Title is too long, it can be up to 20 characters long')
        } else {
            setTitleError(false)
            setTitleMessage('')
            setCategoryData({
                ...categoryData,
                title : e.target.value.trim()
            })
        }
    }

    function notNumbers (e) {
        if(!isNaN(e.key)){
            e.preventDefault()
            setTitleMessage("This field can't contain any numbers")
            setTimeout(() => {
                setTitleMessage('')
            }, 3000);
        }
    }


    //Description validation
    //Not empty description
    function descriptionLimit(e){
        if(e.target.value.trim().length > 100){
            e.preventDefault()
            setDescriptionMessage('This field has a limit of 100 characters')
            setTimeout(() => {
                setDescriptionMessage('')
            }, 2000);
        }
    }

    function notEmptyDescription(e){
        if(e.target.value.trim().length == 0){
            setDescriptionError(true)
            setDescriptionMessage("This field can't be empty")
        } else if (e.target.value.trim().length > 100){
            setDescriptionError(true)
            setDescriptionMessage("Currently, this field has more than 100 characters")
        } else {
            setDescriptionError(false)
            setDescriptionMessage('')
            setCategoryData({
                ...categoryData,
                description : e.target.value.trim()
            })
        }
    }
    
    function handleDescription (e){
        categoryImagePicked(e)
        descriptionLimit(e)
        notEmptyDescription(e)
    }


    //Image logic and validations
    const [categoryImage, setCategoryImage] = useState()

    const categoryImgInput = (e) => {
        const file = e.currentTarget.files[0]
        setCategoryImage(file)
    }

    function categoryImagePicked (e){
        if(!e.currentTarget.files && !categoryImage){
            setCategoryImageError(true)
            setCategoryImageMessage('You must pick a main photo for this tour')
        } else{
            setCategoryImageError(false)
            setCategoryImageMessage('')
        }
    }

    function onChangeImg(e){
        categoryImgInput(e)
        categoryImagePicked(e)
    }


    //Submit handler
    useEffect(() => {
        function enableSubmit(){
            if(titleError || descriptionError || categoryImageError){
                setSubmitDisabled(true)
            } else {
                setSubmitDisabled(false)
            }
        }

        enableSubmit()
    },[sumbitDisabled, titleError, descriptionError, categoryImageError ])

    function handleSubmit (e){
        e.preventDefault()
        setSubmitLoading(true)
        submit()
        const categoryExists = data.some((c) => c.title === categoryData.title);

        if (!categoryExists) {
            data.push(categoryData);
            const newCategories = JSON.stringify(data);
            localStorage.setItem('users', newCategories);
            console.log("Categoria Agregada");
        } else {
            console.log("Categoria ya existe!");
        }
    }

    const [submitLoading, setSubmitLoading] = useState(false)

const submit = () =>{
    try {
        formData.append("data", JSON.stringify(categoryData));
        formData.append('imageFile', categoryImage)
        const apiRequest = async () => {
            const result = await axios.post('/api/v1/digitalbooking/tourCategory/image', formData,{
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).catch((error) => {
                    console.log(error);
                    console.log(error.response.data);
                    setSubmitLoading(false)
                });
            setData(result.data);
            console.log({ data });
            console.log(result);
            setSubmitLoading(false)
            alert('Category successfully added')
        }
        apiRequest()
        
    } catch (error) {
        console.log('Hubo error: ' + error);
        
    }
}

    //Outclick closer
    const menuRef = useRef();

    useEffect(() => {
        let outClickHandler = (e) => {
            if (!menuRef.current.contains(e.target)) {
                e.preventDefault()
                setAddCategoryDisplayed(false)
            }
        }
        document.addEventListener('mousedown', outClickHandler)

        return () => {
            document.removeEventListener('mousedown', outClickHandler)
        }
    })



    return (

        <div className={styles.categoryFormContainer}>
            <div className={styles.registerCategory} ref={menuRef}>

                <form action="submit" className={styles.addCategoryForm} onSubmit={handleSubmit}>
                    <ul>
                        <h2>Agregar categoria</h2>
                        <li>
                            <label htmlFor="title">Title:</label>
                            <input type="text" name="title" id="title" onKeyDown={notNumbers} onChange={titleLimit} onBlur={titleLimit}/>
                            {titleMessage && <p className={styles.validationError}>{titleMessage}</p>}
                        </li>

                        <li>
                            <label htmlFor="description">Description:</label>
                            <textarea name="description" id="description" rows="4" cols="20" onKeyDown={descriptionLimit} onBlur={handleDescription} onChange={handleDescription}/>
                            {descriptionMessage && <p className={styles.validationError}>{descriptionMessage}</p>}
                        </li>

                        <li className={styles.categoryPhotoInput}>
                            <label className={styles.picsInputs}>
                                <span>Select images</span>
                                <input hidden accept='image/jpg,image/jpeg' type="file" onChange={onChangeImg}/>
                            </label>
                            {categoryImage && <span>{categoryImage.name}</span>}
                            {categoryImageMessage && <p className={styles.validationError}>{categoryImageMessage}</p>}
                        </li>
                    </ul>
                    {submitLoading ? <LoadingAnimated/> : (sumbitDisabled ? <button type='submit' disabled={true}>Complete this form!</button> : <button type='submit' className={styles.submitButton} onMouseOver={categoryImagePicked}>Add Product</button>)}
                </form>
            </div>
        </div>

    )
}

AddCategoryFormScreen.propTypes = {
    setAddCategoryDisplayed: PropTypes.func
}

export default AddCategoryFormScreen;