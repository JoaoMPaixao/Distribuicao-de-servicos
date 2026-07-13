import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'
import FilterBarDistribuicoes from '../components/Distribuicoes/FilterBarDistribuicoes.jsx'
import {
    getCursos,
    criarCurso,
    alterarCursos
} from '../api/cursosApi.jsx'
import {UserContext} from "../UserContext.jsx";
import ModalCursos from "../components/Cursos/ModalCriarCursos.jsx";
import TabelaCursos from "../components/Cursos/TabelaCursos.jsx";
import FilterBarCursos from "../components/Cursos/FilterBarCursos.jsx";

export const CursosPage = () => {
    const {user} = useContext(UserContext)
    const [cursos, setCursos] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)


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

    const handleCriar = async (dados) => {
        const response = await criarCurso(dados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getCursos()
            if (atualizado.success) setCursos(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleAlterar = async (dados) => {
        const response = await alterarCursos(dados)

        if (response.success) {
            setModalAberto(false)
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
        return matchPesquisa
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
                        onCriar={() => setModalAberto(true)} texto="Criar Curso"/>
            <FilterBarCursos pesquisa={pesquisa} setPesquisa={setPesquisa} />
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
                onAlterar={handleAlterar}
            />
            {modalAberto && <ModalCursos onClose={() => setModalAberto(false)} onSubmit={handleCriar}/>}

        </SideBar_TopBar>
    )
}
