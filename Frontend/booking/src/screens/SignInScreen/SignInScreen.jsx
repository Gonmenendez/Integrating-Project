import React, { useState } from 'react'

/* Axios */
import axios from 'axios'

/* Styles */
import styles from './style.module.css'
import { Tooltip } from 'react-tooltip'

/* Redux */
import { useDispatch } from 'react-redux'
import {setIsLoggedIn, setUser} from "../../redux/authSlice";

/* Navigation */
import { useNavigate } from "react-router-dom";

import jwt_decode from "jwt-decode";

function SignInScreen() {
    const history = useNavigate();
    const [pass, setPass] = useState('')
    const [email, setEmail] = useState('')
    const [errorMsg, setErrorMsg] = useState(false)
    const dispatch = useDispatch();

    const handleEmail = (e) => {
        setEmail(e.target.value);
    }

    const handlePass = (e) => {
        setPass(e.target.value);
    }

    function parseJwt(token) {
        return jwt_decode(token)
    }

    function handleSubmit(e) {

        e.preventDefault()

        const usuario = {
            email,
            password: pass
        }

        try {
            const apiRequest = async () => {
                const result = await axios.post(
                  `/api/v1/digitalbooking/api/login`,
                  usuario,
                  {
                    headers: {
                      "Content-Type": "application/json",
                    },
                  }

                )
                .catch((error) => {
                    console.log(error);
                    alert(
                        error.response.data.message ? error.response.data.message : error.response.data.detail +
                        " please try again"
                    );
                    setErrorMsg(error.response.data.fieldErrors);
                });
                if(result.data){
                    const token = parseJwt(result.data.jwtToken);
                    console.log(token);
                    dispatch(
                      setUser({
                        firstName: token.name,
                        lastName: token.lastName,
                        email: email,
                        token: result.data.jwtToken,
                        rol: token.rol,
                      })
                    );
                    dispatch(setIsLoggedIn(true));
                    history("/");
                }
            }
            apiRequest()
        } catch (error) {
            console.log('Hubo error: ' + error);
        }
    }

    return (
      <div className={styles.productFormContainer}>
        <div className={styles.registerUser}>
          <p className={styles.whyLogMsg} data-tip="this is a message"></p>
          <a data-tooltip-id="my-tooltip" data-tooltip-content="You need to log in to make a reservation" className={styles.whyLogMsg}>
          Why log in?
          </a>
          <Tooltip id="my-tooltip" />

          <form
            action="submit"
            className={styles.productForm}
            onSubmit={handleSubmit}
          >
            <ul>
              <li>
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  name="Email"
                  id="email"
                  onChange={handleEmail}
                  required
                />
              </li>
              {errorMsg &&
                errorMsg
                  .filter((a) => a.field == "email")
                  .map((error, index) => (
                    <li key={index} className={styles.error}>
                      {error.error}
                    </li>
                  ))}
              <li>
                <label htmlFor="pass">
                  Password (no less than 8 characters up to 15!):
                </label>
                <input
                  type="password"
                  name="Password"
                  id="pass"
                  onChange={handlePass}
                  minLength={8}
                  maxLength={15}
                  required
                />
              </li>
              {errorMsg &&
                errorMsg.filter((a) => a.field == "password").map(
                  (error, index) => (
                    <li key={index} className={styles.error}>
                      {error.error}
                    </li>
                  )
                )}
              <li>
                <button type="submit">Login!</button>
              </li>
            </ul>
          </form>
        </div>
      </div>
    );
}

export default SignInScreen;