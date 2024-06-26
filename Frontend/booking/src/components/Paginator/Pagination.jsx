import { useState } from "react";
import styles from "./style.module.css";

const Pagination = ({changePage, totalElement}) => {
    const [page,setPage] = useState(0)

    const nextPage = () => {
        const numberPage = Math.round(totalElement / 9);
        if(page + 1 < numberPage){
            setPage(page + 1)
            changePage(page + 1, 9)
        }
    }

    const previousPage = () => {
        if(page > 0){
            setPage(page - 1)
            changePage(page - 1, 9)
        }
    }

    return (
        <div className={styles.pagination}>
            <a className={styles.paginationLink} onClick={previousPage}>&laquo;</a>
            <a className={styles.paginationLink}>{page + 1}</a>
            <a className={styles.paginationLink} onClick={nextPage}>&raquo;</a>
        </div>
    )
}


export default Pagination
