import { instance } from './config.jsx'

export const getDistribuicaoById = async (id) => {
    try {
        const {data} = await instance.get(`v1/distribuicoes/${id}`,id)
        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}


export const getDistribuicoes = async () => {
    try {
       const {data} = await instance.get(`v1/distribuicoes`)
        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const criarDistribuicao = async (dados) => {
    try {

        const {data} = await instance.post(`v1/distribuicoes`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const desativarDistribuicao = async (id) => {
    try {
        await instance.patch(`/v1/distribuicoes/${id}/desativar`)
        return { success: true }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const alterarEstadoDistribuicao = async (id, dados) => {
    try {
        const {data} = await instance.patch(`/v1/distribuicoes/${id}/alterar`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const clonarDistribuicao = async (id) => {

    try{
        await instance.post(`/v1/distribuicoes/${id}/clonar`)
        return { success: true}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const compararDistribuicoes = async (dist1Id, dist2Id) => {
    try {
        const { data } = await instance.get('v1/distribuicoes/comparar', {
            params: { distribuicao1Id: dist1Id, distribuicao2Id: dist2Id }
        })
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}
