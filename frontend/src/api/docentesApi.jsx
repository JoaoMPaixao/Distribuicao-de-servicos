import { instance } from './config.jsx'

export const getDocentes = async () => {

    try{
        const {data} = await instance.get(`v1/docentes`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarDocente = async (dados) => {
    try {

        const {data} = await instance.post(`v1/docentes`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarDocente = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/docentes/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarDocente= async (id) => {

    try {
        const {data} = await instance.delete(`/v1/docentes/${id}/apagar`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}

export const criarContratoDocente = async (dados) => {
    try {

        const {data} = await instance.post(`v1/docentes/contrato`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarContratoDocente = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/docentes/contrato/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

