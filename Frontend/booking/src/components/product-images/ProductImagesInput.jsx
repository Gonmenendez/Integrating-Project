import React, { useState } from 'react'

const ProductImagesInput = () => {
    const [images, setImages] = useState([])

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

    function deleteImg(index){
        const newImgs = images.filter(function (element){
            return element.index !== index
        })
        setImages(newImgs)
    }

    return (
        <div>
            <label>
                <span>Select images</span>
                <input hidden accept='image/jpg' type="file" multiple onChange={changeImgInput}/>
            </label>
        </div>
    )
}

export default ProductImagesInput
