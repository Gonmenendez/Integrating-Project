import React, { useState, useEffect } from 'react'
import { Link, Outlet, useParams } from 'react-router-dom';

/* Axios */
import axios from 'axios'

/* Styles */
import styles from "./style.module.css";

/* Components */
import LoadingAnimated from '@layouts/LoadingAnimated/LoadingAnimated';
import ButtonRemoveProduct from '@components/buttonRemoveProduct/ButtonRemoveProduct'
import TourPicsCarrousel from '@components/tourPicsCarrousel/TourPicsCarrousel';
import RatingSystem from '@/components/RatingSystem/RatingSystem';

/* Navigation */
import {ROUTES} from "@utils/ResourceRoutes";
import ReserveProductMenu from '../../components/ReserveProductMenu/ReserveProductMenu';


const ToursScreen = () => {
  //Tour data
  const [tour, setTour] = useState()
  const [mainImage, setMainImage] = useState()
  const [galleryImages ,setGalleryImages] = useState([])
  //Controllers
  const [galleryDisplayed, setGalleryDisplayed] = useState(false)
  const [loading, setLoading] = useState(true)

  //loading, setloading
  const { id } = useParams();
  useEffect(() => {
    try {
      const apiRequest = async () => {
        const result = await axios(`/api/v1/digitalbooking/tour/${id}`)
        .catch((error) => {
          setLoading(false)
          console.log(error);
          console.log(error.response.data);
      });
        setTour(result.data)
        setMainImage(result.data.images.filter(image => image.isPrincipal == true))
        setGalleryImages(result.data.images.filter(image => image.isPrincipal == false))
        setLoading(false)
      }
      apiRequest()
    } catch (error) {
      console.log('Hubo error: ' + error);
    }
    return () =>{
      setTour()
      setMainImage()
      setGalleryImages([])
    }
  }, [id])

  return (
    <div className={styles.tours}>
      {loading ? (
        <div className={styles.loadingTourContainer}>
          <LoadingAnimated />
          <p>Loading...</p>
        </div>
      ) : tour ? (
        <>
          <div>
            {/* Images section */}
            <div className={styles.imagesContainer}>
              <img
                src={mainImage[0].urlImage}
                alt={tour.name + "main photo"}
                className={styles.mainImage}
              />
              <div className={styles.galleryContainer}>
                {galleryImages.slice(1, 5).map((image, i) => (
                  <div key={image.id} className={styles.imageContainer}>
                    <img
                      src={image.urlImage}
                      className={i == 3 ? styles.imagesOpener : ""}
                    />
                    {i == 3 && (
                      <p
                        className={styles.galleryOpener}
                        onClick={() => setGalleryDisplayed(true)}
                      >{`+${tour.images.length - 4}`}</p>
                    )}
                  </div>
                ))}
              </div>
            </div>
            {/* Tour info section */}
            <div className={styles.info}>
              <div className={styles.detailContainer}>
                <div>
                  <h1>
                    {tour.name[0].toUpperCase() + tour.name.slice(1)}
                    <RatingSystem />
                  </h1>{" "}
                  <p>{tour.description}</p>
                  <ButtonRemoveProduct />
                </div>
                {/* fix availability */}
                <ReserveProductMenu
                  id={parseInt(id)}
                  nameTour={tour.name}
                  imageTour={mainImage[0]}
                  tour={tour}
                />
              </div>
              <hr />
              <Link to={ROUTES.home} className={styles.tourToHome}>
                <i className="bi bi-chevron-left" />
                <p>Home</p>
              </Link>
            </div>
          </div>
          {galleryDisplayed && (
            <TourPicsCarrousel
              images={tour.images}
              setGalleryDisplayed={setGalleryDisplayed}
            />
          )}
        </>
      ) : (
        <div className={styles.tourNotFound}>
          <p className={styles.notFound}>Tour not found.</p>
          <p>{"Either the link's broken or the tour doesn't exist"}</p>
        </div>
      )}
      <Outlet />
    </div>
  );
}

export default ToursScreen
