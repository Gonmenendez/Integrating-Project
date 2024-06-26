import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

/* Axios */
import axios from 'axios'

/* Components */
import NewProductCarousel from '@components/product-carrousel/NewProductCarousel'
import AddCategoryFormScreen from '@screens/AddCategoryFormScreen/AddCategoryFormScreen';
import AssignCategories from "@components/assignCategories/AssignCategories";
import SpecificCard from '@components/specificCard/SpecificCard';
import LoadingAnimated from '@layouts/LoadingAnimated/LoadingAnimated';
import NewTourDates from '@components/newTourDates/NewTourDates';

/* Styles */
import styles from "./style.module.css";

/* Navigation */
import {ROUTES} from "@utils/ResourceRoutes";

/* Date-fns */
import { format } from 'date-fns'

function ProductFormScreen() {
    //Error message and error handler for title
    const [titleError, setTitleError] = useState(true)
    const [titleMessage, setTitleMessage] = useState('')
    //Error message and error handler for main image and gallery images
    const [mainImageError, setMainImageError] = useState(true)
    const [mainImgMessage, setMainImgMessage] = useState('')
    const [picturesError, setPicturesError] = useState(true)
    const [picturesMessage, setPicturesMessage] = useState('')
    //Error message and error handler for short description
    const [shortDescError, setShortDescError] = useState(true)
    const [shortDescMessage, setShortDescMessage] = useState('')
    //Error message and error handler for description
    const [descriptionError, setDescriptionError] = useState(true)
    const [descriptionMessage, setDescriptionMessage] = useState('')
    //Error message and error handler for category assignment
    const [assignCatError, setAssignCatError] = useState(true)
    const [assignCatErrorMessage, setAssignCatErrorMessage] = useState('')
    const [categoriesDisplayed, setCategoriesDisplayed] = useState(false)
    const [addCategoryDisplayed, setAddCategoryDisplayed] = useState(false)
    const [categoriesLoaded, setCategoriesLoaded] = useState(false)
    //Error message and error handler for capacity
    const [capacityError, setCapacityError] = useState(true)
    const [capacityErrorMessage, setCapacityErrorMessage] = useState('')
    //Error message and error handler for dates
    const [datesError, setDatesError] = useState(true)
    const [datesErrorMessage, setDatesErrorMessage] = useState('')
    const [calendarDisplayed, setCalendarDisplayed] = useState(false)
    //Error message and error handler fot start-time
    const [startTimeError, setStartTimeError] = useState(true)
    const [startTimeErrorMessage, setStartTimeErrorMessage] = useState('')
    //Error message and error handler for duration-time
    const [durationTimeError, setDurationTimeError] = useState(true)
    const [durationTimeErrorMessage, setDurationTimeErrorMessage] = useState('')
    //Error message and error handler for price
    const [priceError, setPriceError] = useState(true)
    const [priceErrorMessage, setPriceErrorMessage] = useState('')
    //Handle submit
    const [sumbitDisabled, setSubmitDisabled] = useState(true)
    const [tourData, setTourData] = useState({
        name : '',
        shortDescription: '',
        description: '',
        categories: [],
        price: 0,
        duration: '',
        quantity: 0,
        startTime: '',
        availability: {}
    })
    const formData = new FormData();



    //Title validations
    function titleLimit(e){
        if(e.target.value.trim().length == 0){
            setTitleError(true)
            setTitleMessage("This field can't be empty")
        } else if (e.target.value.trim().length <= 4){
            setTitleError(true)
            setTitleMessage('Title must be at least 5 characters long')
        } else if (e.target.value.trim().length >= 150){
            setTitleError(true)
            setTitleMessage('Title is too long, it can be up to 150 characters long')
        } else {
            setTitleError(false)
            setTitleMessage('')
            setTourData({
                ...tourData,
                name : e.target.value.trim()
            })
        }
    }



    //Images logic and validations
    //Main photo:
    const [mainPhoto, setMainPhoto] = useState()
    const [mainImageSend, setMainImageSend] = useState()

    const mainImgInput = (e) => {
        const file = e.currentTarget.files[0]
        setMainImageSend(file)

        let url = URL.createObjectURL(file)

        const mainImg = {
            name: file.name,
            url,
            file
        }
        e.target.value = null;
        setMainPhoto(mainImg)
    }

    const deleteMainImg = () => {
        setMainImageError(true)
        setMainImgMessage('You must pick a main photo for this tour')
        setMainPhoto()
    }

    //Gallery pictures:
    const [images, setImages] = useState([])
    const [imagesSend, setImagesSend] = useState([])
    const [galleryDisplayed, setGalleryDisplayed] = useState(false)

    const changeImgInput = (e) => {
        let imgIndex;

        if(images.length > 0){
            imgIndex = images[images.length - 1].index + 1
        } else {
            imgIndex = 0
        }

        let newImgsToState = readMultipleImages(e, imgIndex)
        let newImgsState = [...images, ...newImgsToState]
        setImages(newImgsState)
    }

    function readMultipleImages(e, initialIndex){
        const files = e.currentTarget.files
        
        const imgsArraySend = []
        Object.keys(files).forEach((i) => {
            const file = files[i]
            imgsArraySend.push(file)
        })
        setImagesSend(imgsArraySend)
        
        const imgsArray = [];
        Object.keys(files).forEach((i) => {
            const file = files[i]

            let url = URL.createObjectURL(file)

            imgsArray.push({
                index: initialIndex,
                name: file.name,
                url,
                file
            });

            initialIndex++
        })
        return imgsArray
    }

    //Validations
    //Main photo
    function mainPhotoPicked (e){
        if(!e.currentTarget.files && !mainPhoto){
            setMainImageError(true)
            setMainImgMessage('You must pick a main photo for this tour')
        } else{
            setMainImageError(false)
            setMainImgMessage('')
        }
    }

    function onChangeImg(e){
        mainImgInput(e)
        mainPhotoPicked(e)
    }

    //Gallery
    function galleryImagesPicked(e){
        if((e.currentTarget.files ? e.currentTarget.files.length <= 3 : true) && images.length <= 3){
            setPicturesError(true)
            setPicturesMessage('You must pick at least 4 images')
        } else {
            setPicturesError(false)
            setPicturesMessage('')
        }
    }

    function onChangeImages(e){
        changeImgInput(e)
        galleryImagesPicked(e)
    }



    //Short description validations
    //Three hundred characters limit
    function shortDescLimit(e){
        if(e.target.value.trim().length >= 300){
            e.preventDefault()
            setShortDescMessage('This field has a limit of 300 characters')
            setTimeout(() => {
                setShortDescMessage('')
            }, 2000);
        }
    }
    //Neither empty nor over the limit
    function notEmptySD (e){
        if(e.target.value.trim().length == 0){
            setShortDescError(true)
            setShortDescMessage("This field can't be empty")
        } else if(e.target.value.trim().length > 300){
            setShortDescError(true)
            setShortDescMessage("Currently, this field has more than 300 characters")
        } else{
            setShortDescError(false)
            setShortDescMessage('')
            setTourData({
                ...tourData,
                shortDescription: e.target.value.trim()
            })
        }
    }


    //Description validation
    //Not empty description
    function notEmptyDescription(e){
        if(e.target.value.trim().length == 0){
            setDescriptionError(true)
            setDescriptionMessage("This field can't be empty")
        } else {
            setDescriptionError(false)
            setDescriptionMessage('')
            setTourData({
                ...tourData,
                description: e.target.value.trim()
            })
        }
    }


    //Categories logic and validations
    const [categories, setCategories] = useState()

    function categoryAssignment () {
        setTourData({
            ...tourData,
            categories:[]
        })
        setCategoriesDisplayed(true)
    }

    useEffect(()=>{
        try{
            const apiRequest = async () => {
                const result = await axios.get('/api/v1/digitalbooking/tourCategory')
                .catch((error) => {
                    console.log(error);
                    console.log(error.response.data);
                });
                // console.log(result.data);
                setCategories(result.data)
                setCategoriesLoaded(true)
            }
            apiRequest()
        } catch (error){
            console.log('Error');
        }
    },[addCategoryDisplayed])
    
    useEffect(()=>{
        categorySelected()
    },[categoriesDisplayed])
    
    useEffect(()=>{
        setAssignCatErrorMessage('')
    },[])

    //Validation
    function categorySelected(){
        if(tourData.categories.length <= 0){
            setAssignCatError(true)
            setAssignCatErrorMessage('You must pick at least one category for this tour')
        } else {
            setAssignCatError(false)
            setAssignCatErrorMessage('')
        }
    }


    //Dates availability and capacity logic and validations
    const [tourDates, setTourDates] = useState([])

    function capacityHandler(e){
        if(e.target.value.trim().length <= 0){
            setCapacityError(true)
            setCapacityErrorMessage("This field can't be empty")
        } else {
            if(isNaN(e.target.value)){
                setCapacityError(true)
                setCapacityErrorMessage("Only numbers allowed")
            } else {
                setCapacityError(false)
                setCapacityErrorMessage('')
                setTourData({
                ...tourData,
                quantity: parseInt(e.target.value.trim())
                })
            }
        }
    }

    useEffect(()=>{
        const dates = []
        tourDates.map(tourDate => dates.push(format(tourDate, 'yyyy-MM-dd')))

        function dataToObject (dates, capacity) {
            const response = new Object();
            dates.forEach(element => {
                response[element] = capacity
            });
            return response
        }

        setTourData({
            ...tourData,
            availability: dataToObject(dates, tourData.quantity)
        })
    },[tourData.quantity, tourDates])

    function tourDatesController(){
        if(tourDates.length <= 0){
            setDatesError(true)
            setDatesErrorMessage("You must pick at least one date fot this tour")
        } else {
            setDatesError(false)
            setDatesErrorMessage('')
        }
    }


    //Start-time logic and validations
    function startTimePicked(e){
        if(!e.target.value){
            setStartTimeError(true)
            setStartTimeErrorMessage("You must pick a valid hour for tour's start-time")
            setTourData({
                ...tourData,
                startTime: ''
            })
        } else {
            setStartTimeError(false)
            setStartTimeErrorMessage('')
            setTourData({
                ...tourData,
                startTime: e.target.value+':00'
            })
        }
    }


    //Duration-time logic and validations
    function durationTimePicked(e){
        if(!e.target.value){
            setDurationTimeError(true)
            setDurationTimeErrorMessage("You must pick a valid hour for tour's duration-time")
            setTourData({
                ...tourData,
                startTime: ''
            })
        } else {
            setDurationTimeError(false)
            setDurationTimeErrorMessage('')
            setTourData({
                ...tourData,
                duration: e.target.value+':00'
            })
        }
    }


    //Price logic and validations
    function priceHandler(e){
        if(e.target.value.trim().length <= 0){
            setPriceError(true)
            setPriceErrorMessage("This field can't be empty")
        } else {
            if(isNaN(e.target.value)){
                setPriceError(true)
                setPriceErrorMessage("Only numbers allowed")
            } else {
                setPriceError(false)
                setPriceErrorMessage('')
                setTourData({
                ...tourData,
                price: parseInt(e.target.value.trim())
                })
            }
        }
    }

    function onBlurPrice(e){
        mainPhotoPicked(e)
        galleryImagesPicked(e)
        notEmptySD(e)
        notEmptyDescription(e)
        categorySelected()
        capacityHandler(e)
        tourDatesController()
        startTimePicked(e)
        durationTimePicked(e)
        priceHandler(e)
    }


    useEffect(() => {
        function enableSubmit(){
            if(titleError || mainImageError || picturesError || shortDescError || descriptionError || assignCatError || capacityError || datesError || startTimeError || durationTimeError || priceError){
                setSubmitDisabled(true)
            } else {
                setSubmitDisabled(false)
            }
        }

        enableSubmit()
    },[sumbitDisabled, titleError, mainImageError, picturesError, shortDescError, descriptionError, assignCatError, capacityError, datesError, startTimeError, durationTimeError, priceError])

    function handleSubmit (e){
        e.preventDefault()
        setSubmitLoading(true)
        submit()
    }
    
    const [successfulSubmit, setSuccessfulSubmit] = useState(false)
    const [submitLoading, setSubmitLoading] = useState(false)
    const [submitResult, setSubmitResult] = useState({})

    const submit = () => {
        try{
            formData.append("dataTour", JSON.stringify(tourData));
            formData.append("imageMain", mainImageSend);
            imagesSend.map(image => {
                formData.append("imageFiles", image)
            })
            const apiRequest = async () =>{
                const result = await axios.post('/api/v1/digitalbooking/tour/image', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).catch((error) => {
                    console.log(error);
                    console.log(error.response.data);
                    setTitleError(true)
                    setTitleMessage('This title is chosen, try another one')
                    setSubmitLoading(false)
                });
                console.log(result);
                setSubmitResult(result.data)
                setSuccessfulSubmit(true)
                setSubmitLoading(false)
            }
            apiRequest()
        } catch(error){
            console.log('Error: ' + error);
        }
    }

    return (
        <>
        {successfulSubmit ? 
        <div className={styles.successfulSubmit}>
            <p>Your tour has been submitted successfully.</p>
            <SpecificCard submitResult={submitResult}/>
            <div className={styles.successfulSubmitDiv}>
                <Link to={ROUTES.addProduct} target='_blank'>Add another tour</Link>
                <Link to={ROUTES.home}>Go back to homepage</Link>
                <Link to={`/tours/${submitResult.id}`} target='_blank'>Check out your new tour</Link>
            </div>
        </div>
        : 
            <div className={styles.productFormContainer}>
                <div className={styles.addProduct}>
                    <form action="submit" className={styles.productForm} onSubmit={handleSubmit}>

                        <div>
                            <label htmlFor="title">Tour title:</label>
                            <input id='title' type="text" placeholder='Set a tilte for this tour' onChange={titleLimit} onBlur={titleLimit}/>
                            {titleMessage && <p className={styles.validationError}>{titleMessage}</p>}
                        </div>

                        <div className={styles.mainPhotoInput}>
                            <label className={styles.picsInputs}>
                                <span>Select main photo</span>
                                <input hidden accept='image/jpg,image/jpeg' type="file" onChange={onChangeImg}/>
                            </label>
                            {mainPhoto && <span>{mainPhoto.name}</span>}
                            {mainImgMessage && <p className={styles.validationError}>{mainImgMessage}</p>}
                        </div>

                        <div className={styles.picturesInput}>
                            <label className={styles.picsInputs}>
                                <span>Select images</span>
                                <input hidden accept='image/jpg,image/jpeg' type="file" multiple onChange={onChangeImages}/>
                            </label>
                            {picturesMessage && <p className={styles.validationError}>{picturesMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="shortDescription">Short description: </label>
                            <textarea id='shortDescription' type="textarea" placeholder='Add a shot description (up to 300 characters)' onKeyPress={shortDescLimit} onInput={notEmptySD} onBlur={notEmptySD}/>
                            {shortDescMessage && <p className={styles.validationError}>{shortDescMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="description">Description: </label>
                            <textarea id='description' type="textarea" placeholder='Add a description' onChange={notEmptyDescription} onBlur={notEmptyDescription}/>
                            {descriptionMessage && <p className={styles.validationError}>{descriptionMessage}</p>}
                        </div>

                        <div>    
                            <div className={styles.categoriesProduct}>
                                <span className={styles.assignCategory} onClick={categoryAssignment}>Assign categories</span>
                                <span className={styles.assignCategory} onClick={() => setAddCategoryDisplayed(true)}>Add new category</span>
                            </div>
                            {tourData.categories && tourData.categories.map(category => <span className={styles.categoriesPicked} key={category.title}>{category.title}</span>)}
                            {assignCatErrorMessage && <p className={styles.validationError}>{assignCatErrorMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="capacity">Capacity per day:</label>
                            <input type="text" id="capacity" placeholder='Set the capacity for each tour-day (numbers only)' onBlur={capacityHandler} onChange={capacityHandler}/>
                            {capacityErrorMessage && <p className={styles.validationError}>{capacityErrorMessage}</p>}
                        </div>

                        <div>
                            <span className={styles.assignCategory} onClick={() => setCalendarDisplayed(!calendarDisplayed)}>Choose dates</span>
                            {datesErrorMessage && <p className={styles.validationError}>{datesErrorMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="startTime">Start-time <i>(HH:MM)</i> :</label>
                            <input type="time" id='startTime' onBlur={startTimePicked} onChange={startTimePicked}/>
                            {startTimeErrorMessage && <p className={styles.validationError}>{startTimeErrorMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="duration">Tour duration:</label>
                            <input type="time" id="duration" onBlur={durationTimePicked} onChange={durationTimePicked}/>
                            {durationTimeErrorMessage && <p className={styles.validationError}>{durationTimeErrorMessage}</p>}
                        </div>

                        <div>
                            <label htmlFor="price">Price:</label>
                            <input id='price' type="text" placeholder='Set the price for this tour (numbers only)' onBlur={onBlurPrice} onChange={priceHandler}/>
                            {priceErrorMessage && <p className={styles.validationError}>{priceErrorMessage}</p>}
                        </div>

                        {submitLoading ? <LoadingAnimated/> : (sumbitDisabled ? <button type='submit' disabled={true}>Complete this form!</button> : <button type='submit' className={styles.submitButton}>Add Product</button>)}
                    </form>
                </div>
                {categoriesDisplayed && <AssignCategories categories={categories} setCategoriesDisplayed={setCategoriesDisplayed} tourData={tourData} setTourData={setTourData} categoriesLoaded={categoriesLoaded} categorySelected={categorySelected}/>}
                {addCategoryDisplayed && <AddCategoryFormScreen setAddCategoryDisplayed={setAddCategoryDisplayed}/>}
                {calendarDisplayed && <NewTourDates setCalendarDisplayed={setCalendarDisplayed} setTourDates={setTourDates} tourDates={tourDates} tourDatesController={tourDatesController}/>}
                <div className={styles.gallery}>
                    <h2>Gallery</h2>
                    {mainPhoto ?
                        <div className={styles.galleryImgContainer}>
                            <img src={mainPhoto.url} alt={mainPhoto.name}/>
                            <button className={styles.deleteMainImg} onClick={deleteMainImg}><i className="bi bi-x-lg"></i></button>
                        </div>
                    : <p className={styles.gallerySkeleton}>Main photo will be displayed here</p>}
                    {images.length > 3 ?
                        <div className={styles.galleryImgContainer} onClick={() => setGalleryDisplayed(!galleryDisplayed)}>
                            <img className={styles.firstGalImg} src={images[0].url} alt={images[0].name} />
                            <p className={styles.galleryOpener}>{`+${images.length}`}</p>
                        </div>
                    : <p className={styles.gallerySkeleton}>Access to gallery will be displayed here</p>}
                </div>
                {galleryDisplayed && <NewProductCarousel images={images} setImages={setImages} setGalleryDisplayed={setGalleryDisplayed} setPicturesError={setPicturesError} setPicturesMessage={setPicturesMessage} />}
            </div>
        }
        </>
    );
}

export default ProductFormScreen;