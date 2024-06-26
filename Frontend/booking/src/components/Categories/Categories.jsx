// eslint-disable-next-line no-unused-vars
import React, { useEffect, useRef, useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Axios */
import axios from "axios";

/* Link import */
import { Link } from 'react-router-dom'

const Categories = () => {
  //Creating an array of those categories with at least two tours assigned
  const [openCategory, setOpenCategory] = useState('')
  const [selectedCategories, setSelectedCategories] = useState([0, 1, 2, 3])
  const [loading, setLoading] = useState(true)

  //Categories api request
  useEffect(() => {
    try{
      const apiRequest = async () => {
        const result = await axios.get('/api/v1/digitalbooking/tourCategory')
        .catch((error) => {
          setLoading(false)
          console.log(error);
          console.log(error.response.data);
        })
        console.log(result.data);
        console.log(result.data.filter(category => category.tours.length > 1));
        setLoading(false)
        setSelectedCategories(result.data.filter(category => category.tours.length > 1))
      }
      apiRequest()
    } catch (error){
      console.log('Hubo error: ' + error.message);
    }

    return () => {
      setSelectedCategories([0, 1, 2, 3])
    }
}, [])

  //UseRef to click outside
  const categoryRef = useRef()

  useEffect(() => {
    let outClickHandler = (e) =>{
        if(!categoryRef.current.contains(e.target)){
            e.preventDefault()
            setOpenCategory('')
        }
    }
  document.addEventListener('mousedown', outClickHandler)
  
  return() => {
      document.removeEventListener('mousedown', outClickHandler)
  }
  })

// useEffect(()=>{
//   try{
//       const apiRequest = async () => {
//           const result = await axios.get('/api/v1/digitalbooking/tour/category/Tour grupal')
//           console.log(result.data);
//       }
//       apiRequest()
//   } catch (error){
//     console.log(error);
//       console.log('Hubo error: ' + error.message);
//   }
// }, [])



    return (
      <>
      <div className={styles.categoriesTitle}>
        <h2 className={styles.h2categories}>Categories</h2>
        <hr className={styles.categoryHr}/>
      </div>

      <div className={styles.categories}>
        <h1>Tours by interests</h1>
        {/* Categories container */}
        <div className={styles.container}>
          {/* Category */}
          {selectedCategories.map((category, i) => 
            loading ? 
              <div key={i} className={styles.skeletonCategory}>
                <div className={styles.skeletonCategoryTitle}/>
              </div>
            :
            <div onClick={() => setOpenCategory(category.title)} onMouseOver={() => setOpenCategory(category.title)} onMouseOut={() => setOpenCategory('')} ref={categoryRef} className={styles.item} style={{backgroundImage: `url(${category.urlImage})`}} key={category.id}>
              
              {/* Category detail */}
              <div className={styles.categoryDetail}>
                <h3>{category.title}</h3>
                {(openCategory == category.title) && <p className={styles.categoryDescription}>{category.description}</p>}
              </div>

              {/* Tour cards in this category */}
              {(openCategory == category.title) &&
                <>
                <div className={styles.cardsContainer}>
                  {/* First tour card - it'll be replaced with an array preciously filtered with the most recommended results */}
                  <div className={styles.productCard}>
                    <div className={styles.product}>
                      <img src={category.tours[0].images.find(image => image.isPrincipal).urlImage} alt={category.tours[0].name} />
                      {/* Space reserved for tour score */}
                    </div>
                    <div className={styles.productDetail}>
                      <h3>{category.tours[0].name}</h3>
                      <p>{category.tours[0].description.slice(0, 125) + '...'}</p>
                    </div>
                  </div>
                  
                  <hr className={styles.desktopProducts}/>
                  
                  {/* Second tour card */}
                  <div className={`${styles.productCard} ${styles.desktopProducts}`}>
                    <div className={styles.product}>
                      <img src={category.tours[1].images.find(image => image.isPrincipal).urlImage} alt={category.tours[1].name} />
                      {/* Space reserved for tour score */}
                    </div>
                    <div className={styles.productDetail}>
                      <h3>{category.tours[1].name}</h3>
                      <p>{category.tours[1].description.slice(0, 125) + '...'}</p>
                    </div>
                  </div>
                </div>
                <Link to={''}>Learn more...</Link>
                </>
              }
            </div>
          )}
        </div>
        <Link className={styles.moreCategories} to={''}>Explore categories!</Link>
      </div>
      </>
    );
}

export default Categories