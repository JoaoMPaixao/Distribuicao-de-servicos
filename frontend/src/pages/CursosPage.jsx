import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'

import {
    getCursos,
    criarCurso,
    alterarCursos, apagarCurso
} from '../api/cursosApi.jsx'
import {UserContext} from "../UserContext.jsx";

import TabelaCursos from "../components/Cursos/TabelaCursos.jsx";
import FilterBarCursos from "../components/Cursos/FilterBarCursos.jsx";
import {getDepartamentos} from "../api/departamentosApi.jsx";
import ModalCursos from "../components/Cursos/ModalCursos.jsx";


export const CursosPage = () => {
    const {user} = useContext(UserContext)
    const [cursos, setCursos] = useState([])
    const [departamentos, setDepartamentos] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [departamentoFiltro, setDepartamentoFiltro] = useState("")
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)
    const [cursoSelecionado, setCursoSelecionado] = useState(null)


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
        setCursoSelecionado(null)
        setModalAberto(true)
    }

    const abrirEditar = (curso) => {
        setCursoSelecionado(curso)
        setModalAberto(true)
    }

    const handleSubmit = async (idOuDados, dadosSeEdicao) => {

        const response = cursoSelecionado
            ? await alterarCursos(idOuDados, dadosSeEdicao)
            : await criarCurso(idOuDados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getCursos()
            if (atualizado.success) setCursos(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleApagar = async (id) =>{

        const response = await apagarCurso(id)

        if(response.success){

            const atualizado = await getCursos()
            if (atualizado.success) setCursos(atualizado.data)
        } else {
            setErroModal(response.message)
    }

    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, porPagina])


    const cursosFiltrados = cursos.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        const matchDepartamento = !departamentoFiltro || String(d.departamento_id) === departamentoFiltro
        return matchPesquisa && matchDepartamento
    })


    const totalPaginas = Math.max(1, Math.ceil(cursosFiltrados.length / porPagina))
    const cursosPagina = cursosFiltrados.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar>
            <PageHeader titulo="Gestão de cursos" subtitulo="Lista de todos os cursos"
                        onCriar={abrirCriar} texto="Criar Curso"/>
            <FilterBarCursos pesquisa={pesquisa} setPesquisa={setPesquisa} departamentoFiltro = {departamentoFiltro} setDepartamentoFiltro={setDepartamentoFiltro} departamentos={departamentos}/>
            <TabelaCursos
               cursos={cursosPagina}
                porPagina={porPagina}
                onMudarPorPagina={setPorPagina}
                paginacao={{
                    inicio: (pagina - 1) * porPagina + 1,
                    fim: Math.min(pagina * porPagina, cursosFiltrados.length),
                    total: cursos.length,
                    atual: pagina,
                    totalPaginas,
                    onMudar: setPagina
                }}
               onAlterar={abrirEditar}
               onApagar={handleApagar}
            />
            {modalAberto && <ModalCursos curso = {cursoSelecionado} onClose={() => setModalAberto(false)} onSubmit={handleSubmit} departamentos={departamentos}/>}
        </SideBar_TopBar>
    )
}
