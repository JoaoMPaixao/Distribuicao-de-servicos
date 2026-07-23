import { instance } from './config.jsx'

export const getUcsDistribuicao = async (id) => {

    try{
        const {data} = await instance.get(`/v1/distribuicoes/${id}/atribuicoes`)
        console.log(data)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarUcDistribuicaoTurno = async (id,udid) => {
    try {

        const {data} = await instance.post(`/v1/distribuicoes/${id}/atribuicoes/${udid}`);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const criarJuncao = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/distribuicoes/${id}/juncao`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarUcDistribuicao = async (id,udid) => {

    try {
        const {data} = await instance.delete(`/v1/distribuicoes/${id}/delete/${udid}`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}

export const alterarUcDistribuicao = async (id,udid, dados) => {
    try {
        const {data} = await instance.put(`/v1/distribuicoes/${id}/alterar/${udid}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const getDocentesUcsDistribuicao = async () => {

    try{
        const {data} = await instance.get(`/v1/docentes/distribuicao`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

