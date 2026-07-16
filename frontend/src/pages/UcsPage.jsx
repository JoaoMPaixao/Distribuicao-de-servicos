import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'
import {
    getUcs,
    criarUc,
    alterarUcs,
    criarUcTipoAula,
    alterarUcsTipoAula, getTipoAula
} from '../api/ucsApi.jsx'
import {UserContext} from "../UserContext.jsx";

import TabelaUcs from "../components/Ucs/TabelaUcs.jsx";
import FilterBarUcs from "../components/Ucs/FilterBarUcs.jsx";
import {getDepartamentos} from "../api/departamentosApi.jsx";
import {getCursos} from "../api/cursosApi.jsx";
import ModalUcs from "../components/Ucs/ModalUcs.jsx";
import ModalUcsTipoAula from "../components/Ucs/ModalUcsTipoAula.jsx";


export const UcsPage = () => {
    const {user} = useContext(UserContext)
    const [ucs, setUcs] = useState([])
    const [cursos, setCursos] = useState([])
    const [departamentos, setDepartamentos] = useState([])
    const [tipoAulas, setTipoAulas] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [departamentoFiltro, setDepartamentoFiltro] = useState("")
    const [cursoFiltro, setCursoFiltro] = useState("")
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)
    const [ucSelecionado, setUcSelecionado] = useState(null)
    const [ucTipoAulaSelecionado, setUcTipoAulaSelecionado] = useState(null)
    const [modalTipoAulaAberto, setModalTipoAulaAberto] = useState(false)

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getTipoAula()

            if (response.success) {
                setTipoAulas(response.data)

            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getUcs()

            if (response.success) {
                setUcs(response.data)

            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getCursos()

            if (response.success) {
                setCursos(response.data)

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
        setUcSelecionado(null)
        setModalAberto(true)
    }

    const abrirEditar = (uc) => {
        setUcSelecionado(uc)
        setModalAberto(true)
    }

    const abrirUcTipoAulaEditar = (uc) => {
        setUcTipoAulaSelecionado(uc)
        setModalTipoAulaAberto(true)
    }

    const handleSubmit = async (idOuDados, dadosSeEdicao) => {

        const response = ucSelecionado
            ? await alterarUcs(idOuDados, dadosSeEdicao)
            : await criarUc(idOuDados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getUcs()
            if (atualizado.success) setUcs(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleSubmitUcTipoAula = async (idOuDados, dadosSeEdicao) => {
        const response = dadosSeEdicao
            ? await alterarUcsTipoAula(idOuDados, dadosSeEdicao)
            : await criarUcTipoAula(idOuDados)

        if (!response.success) {
            setErroModal(response.message)
        }
    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, porPagina])


    const ucsFiltrados = ucs.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        const matchDepartamento = !departamentoFiltro || String(d.departamento_id) === departamentoFiltro
        const matchCurso = !cursoFiltro || String(d.curso_id) === cursoFiltro
        return matchPesquisa && matchDepartamento && matchCurso
    })


    const totalPaginas = Math.max(1, Math.ceil(ucsFiltrados.length / porPagina))
    const ucsPagina = ucsFiltrados.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar>
            <PageHeader titulo="Gestão de ucs" subtitulo="Lista de todos os ucs"
                        onCriar={abrirCriar} texto="Criar Uc"/>
            <FilterBarUcs pesquisa={pesquisa} setPesquisa={setPesquisa} departamentoFiltro = {departamentoFiltro} setDepartamentoFiltro={setDepartamentoFiltro} departamentos={departamentos} cursoFiltro={cursoFiltro} setCursoFiltro={setCursoFiltro} cursos={cursos}/>
            <TabelaUcs
               ucs={ucsPagina}
                porPagina={porPagina}
                onMudarPorPagina={setPorPagina}
                paginacao={{
                    inicio: (pagina - 1) * porPagina + 1,
                    fim: Math.min(pagina * porPagina, ucsFiltrados.length),
                    total: ucs.length,
                    atual: pagina,
                    totalPaginas,
                    onMudar: setPagina
                }}
                onAlterar={abrirEditar}
               onUcTipoAula={abrirUcTipoAulaEditar}
            />
                {modalAberto && <ModalUcs uc = {ucSelecionado} onClose={() => setModalAberto(false)} onSubmit={handleSubmit} departamentos={departamentos} cursos={cursos}/>}
                {modalTipoAulaAberto && <ModalUcsTipoAula uc={ucTipoAulaSelecionado}  onClose={() => setModalTipoAulaAberto(false)} onSubmit={handleSubmitUcTipoAula} tipoAulas={tipoAulas} />}
        </SideBar_TopBar>
    )
}
