import React, { useState } from 'react'

/* Axios */
import axios from 'axios'

/* Styles */
import styles from './style.module.css'

/* Navigation */
import { useNavigate } from "react-router-dom";

function SignUpScreen() {
    const history = useNavigate();
    const [name, setname] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('')
    const [pass, setPass] = useState('')
    const [errorMessageName, setErrorMessageName] = useState('')
    const [errorMessageLastName, setErrorMessageLastName] = useState('')
    const [isValid, setIsValid] = useState();
    const [errorMsg, setErrorMsg] = useState(false)

    const handleName = (e) => {
        const value = e.target.value;
        setname(value);

        if (/\d/.test(value)) {
            setErrorMessageName('*Please enter a non-numeric value for your name*');
            setTimeout(() => {
                setErrorMessageName('')
            }, 2000);
        } else {
            setErrorMessageName('');
        }
    }

    const handleLastName = (e) => {
        const value = e.target.value;
        setLastName(value);

        if (/\d/.test(value)) {
            setErrorMessageLastName('*Please enter a non-numeric value for your last name*');
            setTimeout(() => {
                setErrorMessageLastName('')
            }, 2000);
        } else {
            setErrorMessageLastName('');
        }
    }

    const handleEmail = (e) => {
        setEmail(e.target.value);
    }

    const handlePass = (e) => {
        const value = e.target.value;
        setPass(value);

        // regex validacion password
        const passwordRegex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,}$/;
        setIsValid(passwordRegex.test(value));
    }

    function handleSubmit(e) {

        e.preventDefault()

        const usuario = {
            name: name,
            lastName : lastName,
            email: email,
            password: pass
        }

        try {
            console.log(JSON.stringify(usuario));
            const apiRequest = async () => {
                const result = await axios
                  .post('/api/v1/digitalbooking/api/sign-up', JSON.stringify(usuario), {
                    headers: {
                        'Content-Type': 'application/json'
                    },
                  })
                  .catch((error) => {
                    setErrorMsg(error.response.data.fieldErrors);
                    alert("Oops, something went wrong: " + error.response.data.detail + " please try again")
                  });
                if (result) {
                    alert("User created successfully, we will send you an email to confirm your account");
                    history("/loginUser");
                }
            }
            apiRequest()
        } catch (error) {
            console.log(error);
        }

    }



    return (
      <div className={styles.productFormContainer}>
        <div className={styles.registerUser}>
          <form
            action="submit"
            className={styles.productForm}
            onSubmit={handleSubmit}
          >
            <ul>
              <li>
                <label htmlFor="name">Name:</label>
                <input
                  type="text"
                  name="name"
                  id="name"
                  value={name}
                  onChange={handleName}
                  pattern="[^\d]+"
                  minLength={3}
                  maxLength={15}
                  required
                />
                {errorMessageName && (
                  <p className={styles.errorMessage}>{errorMessageName}</p>
                )}
              </li>

              <li>
                <label htmlFor="last-name">Last Name:</label>
                <input
                  type="text"
                  name="Apellido"
                  id="last-name"
                  value={lastName}
                  onChange={handleLastName}
                  minLength={3}
                  maxLength={15}
                  required
                />
                {errorMessageLastName && (
                  <p className={styles.errorMessage}>{errorMessageLastName}</p>
                )}
              </li>

              <li>
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  name="Email"
                  id="email"
                  value={email}
                  onChange={handleEmail}
                  required
                />
              </li>
              <li>
                <label htmlFor="pass">Password:</label>
                <input
                  type="password"
                  name="Password"
                  id="pass"
                  value={pass}
                  onChange={handlePass}
                  minLength={6}
                  maxLength={20}
                  required
                />
                {errorMsg &&
                  errorMsg.map(
                    (error, index) =>
                      (
                        <li key={index} className={styles.error}>{error.error}</li>
                      )
                  )}
              </li>
              <li>
                <button type="submit">Register!</button>
              </li>
            </ul>
          </form>
        </div>
      </div>
    );
}

export default SignUpScreen;