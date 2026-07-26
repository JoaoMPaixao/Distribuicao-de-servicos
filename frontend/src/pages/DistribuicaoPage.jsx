import { useState, useEffect, useContext } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {UserContext} from "../UserContext.jsx";
import {
    getUcsDistribuicao,
    criarUcDistribuicaoTurno,
    apagarUcDistribuicao,
    criarJuncao,
    getDocentesUcsDistribuicao, alterarHoras, atribuirDocente, retirarDocente
}
    from '../api/atribuicoesApi.jsx'
import TabelaDistribuicao from "../components/Distribuicao/TabelaAtribuicoes.jsx"
import PainelDocentes from '../components/Distribuicao/PainelDocentes.jsx'
import FilterBarAtribuicoes from "../components/Distribuicao/FilterBarAtribuicoes.jsx";
import SideBarCollapsed_TopBar from "../layouts/SideBarCollapsed_TopBar.jsx";
import {getCursosByDepartamento} from "../api/cursosApi.jsx";
import {alterarEstadoDistribuicao, getDistribuicaoById, getDistribuicoes} from "../api/distribuicoesApi.jsx";
import ModalJuncao from "../components/Distribuicao/ModalJuncao.jsx";



function DistribuicaoPage() {
    const { id } = useParams() // id da distribuição vem da rota
    const { user } = useContext(UserContext)
    const navigate = useNavigate()
    const [distribuicao, setDistribuicao] = useState(null)
    const [atribuicoes, setAtribuicoes] = useState([])
    const [atribuicaoSelecionada, setAtribuicaoSelecionada] = useState([])
    const [docentes, setDocentes] = useState([])
    const [cursos, setCursos] = useState([])
    const [anoCurricularFiltro, setAnoCurricularFiltro] = useState("")
    const [cursoFiltro, setCursoFiltro] = useState("")
    const [docenteFiltro, setDocenteFiltro] = useState("")
    const [semestreFiltro, setSemestreFiltro] = useState("")
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)

    const abrirJuncao = (atribuicao) => {
        setAtribuicaoSelecionada(atribuicao)
        setModalAberto(true)
    }

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getUcsDistribuicao(id)

            if (response.success) {
                setAtribuicoes(response.data)

            } else {
                setErro(response.message)

            }
        }
        carregar()
    }, [user])

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getDistribuicaoById(id)

            if (response.success) {
                setDistribuicao(response.data)

            } else {
                setErro(response.message)

            }
        }
        carregar()
    }, [user])

    useEffect(() => {
        // if (!user) return
        const carregar = async () => {
            const response = await getCursosByDepartamento()

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



    const handleAlterarHoras = async (atribuicaoid, horas) => {

        const dados = { horasAtribuidas: horas }

        const response = await alterarHoras(id,atribuicaoid, dados)
        if (response.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado.success) setAtribuicoes(atualizado.data)
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleAtribuirDocente = async (atribuicaoid, docente) => {

        const dados = { docenteId: docente.id }

        const response = await atribuirDocente(id,atribuicaoid, dados)
        if (response.success) {
          
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado.success) setAtribuicoes(atualizado.data)
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleRetirarDocente = async (atribuicaoid) => {

        const response = await retirarDocente(id,atribuicaoid)
        if (response.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado.success) setAtribuicoes(atualizado.data)
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleClonar = async (atribuicaoId) => {

        const response = await criarUcDistribuicaoTurno(atribuicaoId, id)
        if (response.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado.success) setAtribuicoes(atualizado.data)
        }

    }

    const handleJuncao = async (ucTipoAulaIds) => {
        const dados = {
            ucTipoAulaIds
        }
        const response = await criarJuncao(id, dados)
        console.log(dados)
        if (response?.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado?.success) {
                setAtribuicoes(atualizado.data)
            }
        }
    }

    const atribuicoesFiltradas = atribuicoes.filter(l => {
        const matchAnoCurricular = !anoCurricularFiltro || l.ucAno === anoCurricularFiltro
        const matchCurso = !cursoFiltro || String(l.cursoId) === cursoFiltro
        const matchDocente = !docenteFiltro ||
            (l.docenteNome && l.docenteNome.toLowerCase().includes(docenteFiltro.toLowerCase()));
        const matchSemestre = !semestreFiltro || l.ucSemestre === semestreFiltro
        return matchAnoCurricular && matchCurso && matchSemestre && matchDocente
    })

    if (erro) return <p>Erro: {erro}</p>

    return (
        <SideBarCollapsed_TopBar
          distribuicao={distribuicao}
        >
            <FilterBarAtribuicoes cursos={cursos} anoCurricularFiltro={anoCurricularFiltro}
                                  setAnoCurricularFiltro={setAnoCurricularFiltro}
                                  semestreFiltro={semestreFiltro} setSemestreFiltro={setSemestreFiltro}
                                  cursoFiltro={cursoFiltro} setCursoFiltro={setCursoFiltro}
                                  docenteFiltro={docenteFiltro} setDocenteFiltro={setDocenteFiltro} onLimpar={() => {
                setAnoCurricularFiltro("");
                setCursoFiltro("");
                setSemestreFiltro("")

            }}/>


            <TabelaDistribuicao
                atribuicoes={atribuicoesFiltradas}
                onAtribuirDocente={handleAtribuirDocente}
                onRetirarDocente={handleRetirarDocente}
                onAlterarHoras={handleAlterarHoras}
                onClonar={handleClonar}
                onJuncao={abrirJuncao}
            />

            <PainelDocentes docentes={docentes}/>
            {modalAberto && <ModalJuncao atribuicao = {atribuicaoSelecionada} atribuicoes={atribuicoes} onClose={() => setModalAberto(false)} onSubmit={handleJuncao}/>}
        </SideBarCollapsed_TopBar>


    )
}

export default DistribuicaoPage