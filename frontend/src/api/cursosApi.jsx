import { instance } from './config.jsx'

export const getCursos = async () => {

    try{
        const {data} = await instance.get(`v1/cursos`)
        return { success: true, data}
    }catch (error){
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }

}

export const criarCurso = async (dados) => {
    try {

        const {data} = await instance.post(`v1/cursos`,dados);

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const alterarCursos = async (id, dados) => {
    try {
        const {data} = await instance.put(`/v1/cursos/${id}`, dados)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }
}

export const apagarCurso = async (id) => {

    try {
        const {data} = await instance.delete(`/v1/cursos/${id}/apagar`)
        return { success: true, data }
    } catch (error) {
        return { success: false, status: error.response?.status, message: error.response?.data?.message }
    }

}

