import { instance } from './config.jsx'

export const getDistribuicoes = async (departamentoID) => {
    try {
        const {data} = await instance.get(`v1/departamento/${departamentoID}/distribuicoes`)
        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}

export const criarDistribuicao = async (departamentoID,utilizador_id, dados) => {
    try {
        console.log(departamentoID,utilizador_id, dados)
        const {data} = await instance.post(`v1/departamento/${departamentoID}/distribuicoes`,{

            nome: dados.nome,
            versao: dados.versao,
            ano_letivo: dados.ano_letivo,
            descricao: dados.descricao,
            departamento_id: departamentoID,
            utilizador_id: utilizador_id

        });

        return {success: true, data}
    } catch (error) {
        return {success: false, status: error.response?.status, message: error.response?.data?.message}
    }
}