import {instance} from "./config.jsx";

export const getDepartamentos = async () => {

    try{
        const {data} = await instance.get(`v1/departamentos`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}