import { instance } from './config.jsx'

export const getUcs = async (id) => {

    try{
        const {data} = await instance.get(`v1/ucs`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarUc = async (dados) => {
    try {

        const {data} = await instance.post(`v1/ucs`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarUcs = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/ucs/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarUc = async (id) => {

    try {
        const {data} = await instance.delete(`/v1/ucs/${id}/apagar`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}

export const getUcsTipoAulaByUc = async (id) => {

    try{
        const {data} = await instance.get(`v1/uctipoaulas`,{
            params: { uc_id: id }
        })
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarUcTipoAula = async (dados) => {
    try {

        const {data} = await instance.post(`v1/uctipoaulas`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarUcsTipoAula = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/uctipoaulas/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const getTipoAula = async () => {

    try{
        const {data} = await instance.get(`v1/tipoaulas`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const apagarUcTipoAula = async (id) => {

    try {
        const {data} = await instance.delete(`/v1/uctipoaulas/${id}/apagar`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}

