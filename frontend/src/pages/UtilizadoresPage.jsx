import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'

import {
    getUtilizadores,
    criarUtilizadores,
    alterarUtilizadores
} from '../api/utilizadoresApi.jsx'
import {UserContext} from "../UserContext.jsx";

import TabelaUtilizadores from "../components/Utilizadores/TabelaUtilizadores.jsx";
import FilterBarUtilizadores from "../components/Utilizadores/FilterBarUtilizadores.jsx";
import {getDepartamentos} from "../api/departamentosApi.jsx";
import {getCargos} from "../api/cargosApi.jsx";
import ModalUtilizadores from "../components/Utilizadores/ModalUtilizadores.jsx";


export const UtilizadoresPage = () => {
    const {user} = useContext(UserContext)
    const [utilizadores, setUtilizadores] = useState([])
    const [departamentos, setDepartamentos] = useState([])
    const [cargos, setCargos] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [departamentoFiltro, setDepartamentoFiltro] = useState("")
    const [cargoFiltro, setCargoFiltro] = useState("")
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)
    const [utilizadorSelecionado, setUtilizadorSelecionado] = useState(null)


    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getUtilizadores()

            if (response.success) {
                setUtilizadores(response.data)

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

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getCargos()

            if (response.success) {
                setCargos(response.data)

            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    const abrirCriar = () => {
        setUtilizadorSelecionado(null)
        setModalAberto(true)
    }

    const abrirEditar = (curso) => {
        setUtilizadorSelecionado(curso)
        setModalAberto(true)
    }

    const handleSubmit = async (idOuDados, dadosSeEdicao) => {

        const response = utilizadorSelecionado
            ? await alterarUtilizadores(idOuDados, dadosSeEdicao)
            : await criarUtilizadores(idOuDados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getUtilizadores()
            if (atualizado.success) setUtilizadores(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, porPagina])


    const utilizadoresFiltrados = utilizadores.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        const matchDepartamento = !departamentoFiltro || String(d.departamento_id) === departamentoFiltro
        const matchCargo = !cargoFiltro || String(d.cargo_id) === cargoFiltro
        return matchPesquisa && matchDepartamento && matchCargo
    })


    const totalPaginas = Math.max(1, Math.ceil(utilizadoresFiltrados.length / porPagina))
    const utilizadoresPagina = utilizadoresFiltrados.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar>
            <PageHeader titulo="Gestão de utilizadores" subtitulo="Lista de todos os utilizadores"
                        onCriar={abrirCriar} texto="Criar Utilizador"/>
            <FilterBarUtilizadores pesquisa={pesquisa} setPesquisa={setPesquisa} departamentoFiltro = {departamentoFiltro} setDepartamentoFiltro={setDepartamentoFiltro} departamentos={departamentos} cargoFiltro={cargoFiltro} setCargoFiltro={setCargoFiltro} cargos={cargos}/>
            <TabelaUtilizadores
                utilizadores={utilizadoresPagina}
                porPagina={porPagina}
                onMudarPorPagina={setPorPagina}
                paginacao={{
                    inicio: (pagina - 1) * porPagina + 1,
                    fim: Math.min(pagina * porPagina, utilizadoresFiltrados.length),
                    total: utilizadores.length,
                    atual: pagina,
                    totalPaginas,
                    onMudar: setPagina
                }}
                onAlterar={abrirEditar}
            />
            {modalAberto && <ModalUtilizadores utilizador = {utilizadorSelecionado} onClose={() => setModalAberto(false)} onSubmit={handleSubmit} departamentos={departamentos} cargos={cargos}/>}
        </SideBar_TopBar>
    )
}
