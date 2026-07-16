import {instance} from "./config.jsx";

export const getCargos = async () => {

    try{
        const {data} = await instance.get(`v1/cargos`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}