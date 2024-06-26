import { useEffect, useState } from 'react'

export default function useCountDown () {
    const [ secondsLeft, setSecondsLeft ] = useState(1)

    useEffect(()=>{
        if(secondsLeft <= 0) return;

        const timeout = setTimeout(() => {
            setSecondsLeft(secondsLeft - 1)
        }, 1000);

        return () => clearTimeout(timeout)
    },[secondsLeft])

    function start(seconds){
        setSecondsLeft(seconds)
    }

    return {secondsLeft, start}
}