import React, { useState}  from 'react'

/* Assets */
import lens from '@assets/search.png'

/* Styles */
import styles from './style.module.css'

/* React Router */
import { Link } from 'react-router-dom'

/* Routes */
import { ROUTES } from "@utils/ResourceRoutes";
import { USER_ROLE } from '@utils/constants';

//modal
import AddCategoryFormScreen from "@/screens/AddCategoryFormScreen/AddCategoryFormScreen";

/* Redux hooks */
import { useSelector } from "react-redux";

const Search = () => {
    const isLoggedIn = useSelector((state) => state.authentication.isLoggedIn);
    const user = useSelector((state) => state.authentication.user);
    const [addCategoryDisplayed, setAddCategoryDisplayed] = useState(false)

    const handleSubmit = (e) => {
        e.preventDefault()
    }

    return (
      <div className={styles.body}>
        {isLoggedIn && user.rol == USER_ROLE.ADMIN && (
          <>
            <div className={styles.admin}>
              <h2>Administrator panel</h2>
              <div className={styles.buttons}>
                <Link to={ROUTES.addProduct} className={styles.addTour}>
                  Add new tour!
                </Link>
                <Link
                  className={styles.addTour}
                  onClick={() => setAddCategoryDisplayed(!addCategoryDisplayed)}
                >
                  Add new category!
                </Link>
              </div>
            </div>
          </>
        )}
        {addCategoryDisplayed && (
          <AddCategoryFormScreen
            className={styles.modalContent}
            setAddCategoryDisplayed={setAddCategoryDisplayed}
          />
        )}
        <form className={styles.searchForm} onSubmit={handleSubmit}>
          <div className={styles.searchBar}>
            <input
              type="text"
              name="search"
              id="searchInput"
              placeholder="Enter either an activity or destination"
              className={styles.searchInput}
            />

            <button className={styles.searchButton}>
              <Link to={ROUTES.pagination}>
                  <img src={lens} alt="search" />
              </Link>
            </button>
          </div>
        </form>
      </div>
    );
}

export default Search
