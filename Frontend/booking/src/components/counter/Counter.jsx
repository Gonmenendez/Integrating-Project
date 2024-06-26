import styles from './style.module.css'

const Counter = ({number, numberChange}) => {
    const add = () => {
        numberChange(number + 1)
    }

    const reduce = () => {
        numberChange(number - 1)
    }

    return (
        <div>
            <a className={styles.icons} onClick={reduce}><i className="bi bi-dash"></i></a>
            <a>{number}</a>
            <a className={styles.icons} onClick={add}><i className="bi bi-plus"></i></a>
        </div>
    )
}

export default Counter
