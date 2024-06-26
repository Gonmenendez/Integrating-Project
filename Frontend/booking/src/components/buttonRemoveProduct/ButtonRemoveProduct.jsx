import React, { useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom';

/* Axios */
import axios from 'axios';

/* Styles */
import styles from './buttonRemoveProduct.module.css';

/* Components */
import LoadingAnimated from '@layouts/LoadingAnimated/LoadingAnimated';

/* Redux hooks */
import { useSelector } from "react-redux";

/* Navigation */
import {ROUTES} from "@utils/ResourceRoutes";
import {USER_ROLE} from "@utils/constants";

function ButtonRemoveProduct() {
  const [loading, setLoading] = useState(false)
  const [successMessage, setSuccessMessage] = useState('')

  const navigate = useNavigate()

    const params = useParams();

    const handleRemove = (e) => {
      e.preventDefault()
      setLoading(true)

        try{
            const pedidoApi = async () => {
                const result = await axios.delete(`/api/v1/digitalbooking/tour/${params.id}`)
                .catch((error) => {
                  console.log(error);
                  console.log(error.response.data);
                  setLoading(false)
                })
                console.log(result.data);
                setLoading(false)
                setSuccessMessage(`This tour has been deleted. You'll be redirected to home screen`)
                setTimeout(() => {
                  navigate(ROUTES.home)
                }, 3000);
            }
            pedidoApi()
        } catch (error){
            console.log('Hubo error: ' + error.message);
        }
    }

    const isLoggedIn = useSelector((state) => state.authentication.isLoggedIn);
    const user = useSelector((state) => state.authentication.user);

    return (
      <>
        {isLoggedIn && user.rol == USER_ROLE.ADMIN  && (
          loading ?
            <LoadingAnimated/>
          :
            successMessage ?
              <p className={styles.deletedSuccess}>{successMessage}</p>
            :
              <div className={styles.container}>
                <button onClick={handleRemove}>Remove Tour</button>
              </div>
        )}
      </>
    );
}

export default ButtonRemoveProduct;