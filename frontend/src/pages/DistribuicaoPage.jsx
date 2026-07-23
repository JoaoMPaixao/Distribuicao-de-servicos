import { useState, useEffect, useContext } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {UserContext} from "../UserContext.jsx";
import {
    getUcsDistribuicao,
    criarUcDistribuicaoTurno,
    alterarUcDistribuicao,
    apagarUcDistribuicao,
    criarJuncao,
    getDocentesUcsDistribuicao}
    from '../api/atribuicoesApi.jsx'
import TabelaDistribuicao from "../components/Distribuicao/TabelaAtribuicoes.jsx"
import PainelDocentes from '../components/Distribuicao/PainelDocentes.jsx'
import BarraInferior from '../components/Distribuicao/BottomBar.jsx'
import SideBar_TopBar from "../layouts/SideBar_TopBar.jsx";
import FilterBarAtribuicoes from "../components/Distribuicao/FilterBarAtribuicoes.jsx";
import TopBar from "../components/TopBar.jsx";
import {getDistribuicaoById} from "../api/distribuicoesApi.jsx";
import SideBarCollapsed_TopBar from "../layouts/SideBarCollapsed_TopBar.jsx";
import {getCursos} from "../api/cursosApi.jsx";



function DistribuicaoPage() {
    const { id } = useParams() // id da distribuição vem da rota
    const { user } = useContext(UserContext)
    const navigate = useNavigate()
    const [distribuicao, setDistribuicao] = useState(null)
    const [atribuicoes, setAtribuicoes] = useState([])
    const [docentes, setDocentes] = useState([])
    const [cursos, setCursos] = useState([])
    const [alteracoesPorGuardar, setAlteracoesPorGuardar] = useState(false)
    const [anoCurricularFiltro, setAnoCurricularFiltro] = useState("")
    const [cursoFiltro, setCursoFiltro] = useState("")
    const [semestreFiltro, setSemestreFiltro] = useState("")
    const [erro, setErro] = useState(null)


    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getUcsDistribuicao(id)
            console.log("tentar")
            if (response.success) {
                setAtribuicoes(response.data)
                console.log(response.data)
            } else {
                setErro(response.message)
                console.log("no")
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
            const response = await getDocentesUcsDistribuicao()

            if (response.success) {
                setDocentes(response.data)


            } else {
                setErro(response.message)

            }
        }
        carregar()
    }, [user])


    const handleAtribuirDocente = async (linhaId, docente) => {
        // atualiza localmente primeiro (optimistic update — resposta instantânea)
        setAtribuicoes(prev => prev.map(l => l.id === linhaId ? { ...l, docente } : l))

        // depois persiste na API
        const response = await atribuirDocente(id, linhaId, docente.id)
        if (!response.success) {
            // se falhar, reverte
            setLinhas(prev => prev.map(l => l.id === linhaId ? { ...l, docente: null } : l))
            showToast('Erro ao atribuir docente')
        }
    }

    const handleRemoverDocente = async (linhaId) => {
        const docenteAnterior = linhas.find(l => l.id === linhaId)?.docente
        setLinhas(prev => prev.map(l => l.id === linhaId ? { ...l, docente: null } : l))

        const response = await removerDocente(id, linhaId)
        if (!response.success) {
            setLinhas(prev => prev.map(l => l.id === linhaId ? { ...l, docente: docenteAnterior } : l))
            showToast('Erro ao remover docente')
        }
    }

    const handleAlterarHoras = async (linhaId, horas) => {
        const horasAnterior = linhas.find(l => l.id === linhaId)?.horas
        setLinhas(prev => prev.map(l => l.id === linhaId ? { ...l, horas } : l))

        const response = await atualizarHoras(id, linhaId, horas)
        if (!response.success) {
            setLinhas(prev => prev.map(l => l.id === linhaId ? { ...l, horas: horasAnterior } : l))
            showToast('Erro ao atualizar horas')
        }
    }

    const handleClonar = async (id) => {

        const response = await criarUcDistribuicaoTurno(id)
        if (response.success) {
            const atualizado = await getUcsDistribuicao()
            if (atualizado.success) setAtribuicoes(atualizado.data)
        }

    }


    const atribuicoesFiltradas = atribuicoes.filter(l => {
        const matchAnoCurricular = !anoCurricularFiltro || l.ano_curricular === anoCurricularFiltro
        const matchCurso = !cursoFiltro || String(l.curso_id) === cursoFiltro
        const matchSemestre = !semestreFiltro || l.semestre === semestreFiltro
        return matchAnoCurricular && matchCurso && matchSemestre
    })

    if (erro) return <p>Erro: {erro}</p>

    return (
        <SideBarCollapsed_TopBar
            breadcrumb={[
                { label: 'Distribuições', path: '/distribuicoes' },

            ]}
        >
            <FilterBarAtribuicoes cursos={cursos} anoCurricularFiltro={anoCurricularFiltro} setAnoCurricularFiltro={setAnoCurricularFiltro}
                                  semestreFiltro={semestreFiltro} setSemestreFiltro={setSemestreFiltro}
                                  cursoFiltro={cursoFiltro} setCursoFiltro={setCursoFiltro} onLimpar={() => {
            }}/>


                <TabelaDistribuicao
                    atribuicoes={atribuicoesFiltradas}
                    onAtribuirDocente={handleAtribuirDocente}
                    onRemoverDocente={handleRemoverDocente}
                    onAlterarHoras={handleAlterarHoras}
                    onClonar={handleClonar}
                />

            <PainelDocentes docentes={docentes} />
        </SideBarCollapsed_TopBar>


    )
}

export default DistribuicaoPage