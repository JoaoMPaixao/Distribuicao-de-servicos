import { instance } from './config.jsx'

export const getUcsDistribuicao = async (id) => {

    try{
        const {data} = await instance.get(`/v1/distribuicoes/${id}/atribuicoes`)

        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarUcDistribuicao = async (udid, id) => {
    try {

        const {data} = await instance.post(`/v1/distribuicoes/${id}/atribuicao/${udid}/duplicar`);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const criarUcDistribuicaoTurno = async (udid, id) => {
    try {

        const {data} = await instance.post(`/v1/distribuicoes/${id}/atribuicao/${udid}/turno`);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const criarJuncao = async (id, dados) => {
    try {
        const {data} = await instance.post(`/v1/distribuicoes/${id}/juncao`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarUcJuncao = async (id,udid) => {

    try {
        const {data} = await instance.delete(`/v1/distribuicoes/${id}/deletejuncao/${udid}`)
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

export const alterarHoras = async (id,udid, dados) => {
    try {
        const {data} = await instance.patch(`/v1/distribuicoes/${id}/horas/${udid}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const atribuirDocente = async (id,udid, dados) => {
    try {
        const {data} = await instance.patch(`/v1/distribuicoes/${id}/docente/${udid}/atribuir`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const retirarDocente = async (id,udid) => {
    try {
        const {data} = await instance.patch(`/v1/distribuicoes/${id}/docente/${udid}/retirar`)
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

