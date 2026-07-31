import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'

import {
    getDocentes,
    criarDocente,
    criarContratoDocente,
    alterarDocente,
    apagarDocente, alterarContratoDocente
} from '../api/docentesApi.jsx'
import {UserContext} from "../UserContext.jsx";

import TabelaDocentes from "../components/Docentes/TabelaDocentes.jsx";
import FilterBarDocentes from "../components/Docentes/FilterBarDocentes.jsx";
import {getDepartamentos} from "../api/departamentosApi.jsx";
import ModalDocentes from "../components/Docentes/ModalDocentes.jsx";
import ModalContratoDocente from "../components/Docentes/ModalContratoDocente.jsx";


export const DocentesPage = () => {
    const {user} = useContext(UserContext)
    const [docentes, setDocentes] = useState([])
    const [departamentos, setDepartamentos] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [departamentoFiltro, setDepartamentoFiltro] = useState("")
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [modalContratoAberto, setModalContratoAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)
    const [docenteSelecionado, setDocenteSelecionado] = useState(null)
    const [modoEdicaoContrato, setModoEdicaoContrato] = useState(false)


    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getDocentes()

            if (response.success) {
                setDocentes(response.data)

            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getDepartamentos()

            if (response.success) {
                setDepartamentos(response.data)

            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    const abrirCriar = () => {
        setDocenteSelecionado(null)
        setModalAberto(true)
    }

    const abrirEditar = (docente) => {
        setDocenteSelecionado(docente)
        setModalAberto(true)
    }

    const abrirContrato = (docente, modoEdicao = false) => {
        setDocenteSelecionado(docente)
        setModoEdicaoContrato(modoEdicao)
        setModalContratoAberto(true)
    }

    const handleSubmit = async (idOuDados, dadosSeEdicao) => {

        const response = docenteSelecionado
            ? await alterarDocente(idOuDados, dadosSeEdicao)
            : await criarDocente(idOuDados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getDocentes()
            if (atualizado.success) setDocentes(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleSubmitContrato = async (idOuDados, dadosSeEdicao) => {

        const response = dadosSeEdicao
            ? await alterarContratoDocente(idOuDados, dadosSeEdicao)
            : await criarContratoDocente(idOuDados)

        if (response.success) {
            setModalContratoAberto(false)
            const atualizado = await getDocentes()
            if (atualizado.success) setDocentes(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleApagar = async (id) =>{

        const response = await apagarDocente(id)

        if(response.success){

            const atualizado = await getDocentes()
            if (atualizado.success) setDocentes(atualizado.data)
        } else {
            setErroModal(response.message)
        }

    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, porPagina])


    const docentesFiltrados = docentes.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        const matchDepartamento = !departamentoFiltro || String(d.departamento_id) === departamentoFiltro
        return matchPesquisa && matchDepartamento
    })


    const totalPaginas = Math.max(1, Math.ceil(docentesFiltrados.length / porPagina))
    const docentesPagina = docentesFiltrados.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar>
            <PageHeader titulo="Gestão de docentes" subtitulo="Lista de todos os docentes"
                        onCriar={abrirCriar} texto="Criar Docente"/>
            <FilterBarDocentes pesquisa={pesquisa} setPesquisa={setPesquisa} departamentoFiltro = {departamentoFiltro} setDepartamentoFiltro={setDepartamentoFiltro} departamentos={departamentos}/>
            <TabelaDocentes
                docentes={docentesPagina}
                porPagina={porPagina}
                onMudarPorPagina={setPorPagina}
                paginacao={{
                    inicio: (pagina - 1) * porPagina + 1,
                    fim: Math.min(pagina * porPagina, docentesFiltrados.length),
                    total: docentes.length,
                    atual: pagina,
                    totalPaginas,
                    onMudar: setPagina
                }}
                onAlterar={abrirEditar}
                onAbrirContrato={abrirContrato}
                onApagar={handleApagar}
            />
            {modalAberto && <ModalDocentes docente = {docenteSelecionado} onClose={() => setModalAberto(false)} onSubmit={handleSubmit} departamentos={departamentos}/>}
            {modalContratoAberto && (
                <ModalContratoDocente
                    docente={docenteSelecionado}
                    isEdicao={modoEdicaoContrato}
                    onClose={() => setModalContratoAberto(false)}
                    onSubmit={handleSubmitContrato}
                />
            )}
        </SideBar_TopBar>
    )
}
