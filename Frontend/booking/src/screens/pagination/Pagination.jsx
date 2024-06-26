import React, { useEffect, useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Axios */
import axios from "axios";

/* Link */
import { Link } from 'react-router-dom'

/* Components */
import LoadingAnimated from '@layouts/LoadingAnimated/LoadingAnimated';

const Pagination = () => {
    const [tours, setTours] = useState([])
    const [loading, setLoading] = useState(true)
    const [currentPage, setCurrentPage] = useState(1)
    const [toursShown, setToursShown] = useState([])

    
    useEffect(()=>{
        try{
            const pedidoApi = async () => {
                const result = await axios.get('/api/v1/digitalbooking/tour')
                .catch((error) =>{
                    console.log(error);
                    setLoading(false)
                })
                setTours(result.data)
                setLoading(false)
            }
            pedidoApi()
        } catch (error){
            console.log(error.message);
        }
        
        return () => {
            setTours([])
            setLoading(true)
        }
    }, [])

    const nextPage = () => {
        if(toursShown.length != 0){
            if(currentPage + 1){
                setCurrentPage(currentPage + 1)
            }
        }
    }

    const previousPage = () => {
        if(currentPage != 1){
            if(currentPage > 0){
                setCurrentPage(currentPage - 1)
            }
        }
    }
    
    useEffect(() => {
        const page = currentPage * 10
        if(currentPage == 1){
            setToursShown(tours.slice((currentPage - 1), (currentPage + 11)))
        } else {
            setToursShown(tours.slice((page - 10), page + 1))
        }
    },[currentPage, tours])

    return (
        <>
        {loading ? 
            <div className={styles.loadingTourContainer}>
                <LoadingAnimated/>
                <p>Loading...</p>
            </div>
            : 
            <div>
                <div className={styles.toursContainer}>
                    {toursShown.map(tour => 
                        <Link key={tour.id} to={`/tours/${tour.id}`}>
                            <div className={styles.tourContainer}>
                                <img src={tour.images.find(image => image.isPrincipal)?.urlImage} alt={tour.name}/>
                                <div className={styles.tourDetailContainer}>
                                    <div className={styles.textContainer}>
                                        <h1>{tour.name}</h1>
                                        <p>{tour.shortDescription}</p>
                                    </div>
                                    <div className={styles.priceScore}>
                                        <h6>${tour.price}</h6>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    )}
                    <div className={styles.pagination}>
                        <a className={styles.paginationLink} onClick={previousPage}><i className="bi bi-chevron-left" /></a>
                        <a className={styles.paginationLink}>{currentPage}</a>
                        <a className={styles.paginationLink} onClick={nextPage}><i className="bi bi-chevron-right"/></a>
                    </div>
                </div>
            </div>
        }
        </>
        )
    }

export default Pagination
