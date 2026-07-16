import {instance} from "./config.jsx";

export const getDepartamentos = async () => {

    try{
        const {data} = await instance.get(`v1/departamentos`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarDepartamento = async (dados) => {
    try {

        const {data} = await instance.post(`v1/departamentos`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarDepartamentos = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/departamentos/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}
