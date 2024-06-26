// eslint-disable-next-line no-unused-vars
import React, { useEffect, useRef, useState } from 'react'

/* Prop Types */
import PropTypes from 'prop-types'

/* Styles */
import styles from "./style.module.css";

const TourPicsCarrousel = ({images, setGalleryDisplayed}) => {
    const imagesUrls = images.map(image => image.urlImage)

    const [selectedIndex, setSelectedIndex] = useState(0)
    const [selectedImage, setSelectedImage] = useState(imagesUrls[0])

    const selectedImages = (index, images, next = true) =>{
        const condition = next ? selectedIndex < images.length - 1 : selectedIndex > 0
        const nextIndex = next ? (condition ? selectedIndex + 1 : 0) : condition ? selectedIndex - 1 : images.length - 1
        setSelectedImage(images[nextIndex])
        setSelectedIndex(nextIndex)
    }

    const previous = () => {
        selectedImages(selectedIndex, imagesUrls, false)
    }

    const next = () => {
        selectedImages(selectedIndex, imagesUrls, true)
    }

    const menuRef = useRef()

    useEffect(() => {
        let outClickHandler = (e) =>{
            if(!menuRef.current.contains(e.target)){
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
                    <img src={selectedImage}/>
                </div>
                <button className={styles.carouselButtons} onClick={next}><i className="bi bi-chevron-right"></i></button>
            </div>
        </div>
    )
}

TourPicsCarrousel.propTypes = {
    images: PropTypes.array,
    setGalleryDisplayed: PropTypes.func,
}

export default TourPicsCarrousel