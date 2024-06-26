import axios from 'axios'

const cliente = axios.create({
    baseURL: "http://3.21.247.228:80/api/v1/digitalbooking"
})

export default cliente