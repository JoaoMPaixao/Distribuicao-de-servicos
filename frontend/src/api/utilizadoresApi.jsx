import {instance} from "./config.jsx";

export const getUtilizadores = async () => {

    try{
        const {data} = await instance.get(`v1/utilizadores`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarUtilizadores = async (dados) => {
    try {

        const {data} = await instance.post(`v1/utilizadores`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarUtilizadores = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/utilizadores/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarUtilizador = async (id) => {

    try {
        const {data} = await instance.delete(`/v1/utilizadores/${id}/apagar`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}