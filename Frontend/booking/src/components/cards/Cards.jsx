import React, { useEffect, useState } from 'react'

/* Styles */
import styles from './style.module.css'

/* Axios */
import axios from "axios";

/* Components */
import { Link } from 'react-router-dom'


const Cards = () => {
    const [articles, setArticles] = useState([1, 2, 3, 4, 5, 6, 7, 8, 9])
    const [loading, setLoading] = useState(true)

    useEffect(()=>{
        try{
            const pedidoApi = async () => {
                const result = await axios.get('/api/v1/digitalbooking/tour/random-tours/9')
                console.log(result.data);
                setArticles(result.data)
                setLoading(false)
            }
            pedidoApi()
        } catch (error){
            console.log('Hubo error: ' + error.message);
        }

        return () => {
            setArticles([1, 2, 3, 4, 5, 6, 7, 8, 9])
            setLoading(true)
        }
    }, [])

    return (
        <div className={styles.recommendations}>
            {articles.map((article, i) =>
                loading ?
                    <div className={styles.articleSkeleton} key={i}>
                        <div className={`${styles.articleImgSkeleton} ${i > 2 ? (i < 6 ? styles.responsiveMobile : styles.responsiveTablet) : ''}`}/>
                        <div className={`${styles.articlePSkeleton} ${i > 2 ? (i < 6 ? styles.responsiveMobile : styles.responsiveTablet) : ''}`}/>
                    </div>
                :
                    <div className={`${styles.article} ${i > 2 ? (i < 6 ? styles.responsiveMobile : styles.responsiveTablet) : ''}`} key={article.id}>
                        <Link to={`/tours/${article.id}`}>
                            <div>
                                {article.images.length > 0 && <img src={article.images.find(image => image.isPrincipal).urlImage} alt={article.name}/>}
                                <h3 className={styles.h3cards}>{article.name}</h3>
                                <p className={styles.desktopDescription}>{article.shortDescription}</p>
                            </div>
                            <p className={styles.responsiveDescription}>{article.shortDescription}</p>
                        </Link>
                    </div>
            )}
        </div>
    )
}

export default Cards
