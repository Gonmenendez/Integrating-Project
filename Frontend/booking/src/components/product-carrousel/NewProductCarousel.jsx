// eslint-disable-next-line no-unused-vars
import React, { useEffect, useRef, useState } from 'react'

/* Prop Types */
import PropTypes from 'prop-types'

/* Styles */
import styles from "./style.module.css";

const NewProductCarousel = ({images, setImages, setGalleryDisplayed, setPicturesError, setPicturesMessage}) => {
    const [selectedIndex, setSelectedIndex] = useState(0)
    const [selectedImage, setSelectedImage] = useState(images[0])

    const selectedImages = (index, images, next = true) =>{
        const condition = next ? selectedIndex < images.length - 1 : selectedIndex > 0
        const nextIndex = next ? (condition ? selectedIndex + 1 : 0) : condition ? selectedIndex - 1 : images.length - 1
        setSelectedImage(images[nextIndex])
        setSelectedIndex(nextIndex)
    }

    const previous = () => {
        selectedImages(selectedIndex, images, false)
    }

    const next = () => {
        selectedImages(selectedIndex, images, true)
    }

    function deleteImg(){
        const newImgs = images.filter(function (element){
            return element.index !== selectedImage.index
        })
        setImages(newImgs)
        next()
        previous()
        newImgs.length <= 0 ? setGalleryDisplayed(false) : ''
    }

    useEffect(() => {
        if(images.length < 4){
            setPicturesError(true)
            setPicturesMessage('You must pick at least 4 images')
        }
    }, [images, setPicturesError, setPicturesMessage])

    const menuRef = useRef()

    useEffect(() => {
        let outClickHandler = (e) =>{
            if(!menuRef.current.contains(e.target)){
                if(images.length < 4){
                    setImages([])
                }
                e.preventDefault()
                setGalleryDisplayed(false)
            }
        }

        document.addEventListener('mousedown', outClickHandler)

        return() => {
            document.removeEventListener('mousedown', outClickHandler)
        }
    })

    return (
        <div className={styles.carousel}>
            <div className={styles.carouselContainer} ref={menuRef}>
                <button className={styles.carouselButtons} onClick={previous}><i className="bi bi-chevron-left"></i></button>
                <div className={styles.imgContainer}>
                    <img src={selectedImage.url} alt={selectedImage.name} />
                    <button className={styles.deleteImg} onClick={deleteImg}>Delete</button>
                </div>
                <button className={styles.carouselButtons} onClick={next}><i className="bi bi-chevron-right"></i></button>
            </div>
        </div>
    )
}

NewProductCarousel.propTypes = {
    images: PropTypes.array,
    setImages: PropTypes.func,
    setGalleryDisplayed: PropTypes.func,
    setPicturesError: PropTypes.func,
    setPicturesMessage: PropTypes.func
}

export default NewProductCarousel