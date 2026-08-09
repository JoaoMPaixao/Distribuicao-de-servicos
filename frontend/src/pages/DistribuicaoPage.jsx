import {useState, useEffect, useContext, useMemo} from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import {UserContext} from "../UserContext.jsx";
import {
    getUcsDistribuicao,
    criarUcDistribuicaoTurno,
    apagarUcDistribuicao,
    criarJuncao,
    getDocentesUcsDistribuicao, alterarHoras, atribuirDocente, retirarDocente, apagarUcJuncao, criarUcDistribuicao,
    getVerificarDistribuicao
}
    from '../api/atribuicoesApi.jsx'
import TabelaDistribuicao from "../components/Distribuicao/TabelaAtribuicoes.jsx"
import PainelDocentes from '../components/Distribuicao/PainelDocentes.jsx'
import FilterBarAtribuicoes from "../components/Distribuicao/FilterBarAtribuicoes.jsx";
import SideBarCollapsed_TopBar from "../layouts/SideBarCollapsed_TopBar.jsx";
import {getCursosByDepartamento} from "../api/cursosApi.jsx";
import {getDistribuicaoById} from "../api/distribuicoesApi.jsx";
import ModalJuncao from "../components/Distribuicao/ModalJuncao.jsx";
import ModalVerificacao from "../components/Distribuicao/ModalVerificacao.jsx";
import BarraInferior from "../components/Distribuicao/BottomBar.jsx";



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
    const [modalVerificacaoAberto, setModalVerificacaoAberto] = useState(false)
    const [resultadoVerificacao, setResultadoVerificacao] = useState(null)

    const abrirJuncao = (atribuicao) => {
        setAtribuicaoSelecionada(atribuicao)
        setModalAberto(true)
    }

    useEffect(() => {
        if (!user) return

        const carregar = async () => {
            // todos em paralelo com Promise.all
            const [resAtribuicoes, resDistribuicao, resCursos, resDocentes] = await Promise.all([
                getUcsDistribuicao(id),
                getDistribuicaoById(id),
                getCursosByDepartamento(),
                getDocentesUcsDistribuicao(id)
            ])

            if (resAtribuicoes.success) setAtribuicoes(resAtribuicoes.data)
            if (resDistribuicao.success) setDistribuicao(resDistribuicao.data)
            if (resCursos.success) setCursos(resCursos.data)
            if (resDocentes.success) setDocentes(resDocentes.data)

            const erros = [resAtribuicoes, resDistribuicao, resCursos, resDocentes]
                .find(r => !r.success)
            if (erros) setErro(erros.message)
        }

        carregar()
    }, [user, id]) // ← adiciona id como dependência

    // Função reutilizável para atualizar a lista de atribuições e docentes
    const recarregarDados = async () => {
        const [resUcs, resDocentes] = await Promise.all([
            getUcsDistribuicao(id),
            getDocentesUcsDistribuicao(id)
        ]);

        if (resUcs.success) {
            setAtribuicoes(resUcs.data);
        } else {
            setErro(resUcs.message);
        }

        if (resDocentes.success) {
            setDocentes(resDocentes.data);
        } else {
            setErro(resDocentes.message);
        }
    };

    const handleAlterarHoras = async (atribuicaoid, horas) => {

        const dados = { horasAtribuidas: horas }

        const response = await alterarHoras(id,atribuicaoid, dados)
        if (response.success) {
           await recarregarDados()
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleAtribuirDocente = async (atribuicaoid, docente) => {

        const dados = { docenteId: docente.id }

        const response = await atribuirDocente(id,atribuicaoid, dados)
        if (response.success) {
            await recarregarDados()
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleRetirarDocente = async (atribuicaoid) => {

        const response = await retirarDocente(id,atribuicaoid)
        if (response.success) {
            await recarregarDados()
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleDuplicar = async (atribuicaoId) => {

        const response = await criarUcDistribuicao(atribuicaoId, id)
        if (response.success) {
            await recarregarDados()
        }

    }

    const handleCriarTurno = async (atribuicaoId) => {

        const response = await criarUcDistribuicaoTurno(atribuicaoId, id)
        if (response.success) {
            await recarregarDados()
        }

    }

    const handleApagarTurno = async (ucDistribuicaoId) => {
        const response = await apagarUcDistribuicao(id, ucDistribuicaoId)
        if (response.success) {
            await recarregarDados()
        } else {
            alert(`Erro ao apagar turno: Apague o turno mais recente primeiro`)
        }
    }

    const handleJuncao = async (ucTipoAulaIds) => {
        const dados = {
            ucTipoAulaIds
        }
        const response = await criarJuncao(id, dados)

        if (response?.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado?.success) {
                setAtribuicoes(atualizado.data)
            }
        } else {
            alert(`Erro ao alterar: ${response.message || 'Erro desconhecido'}`)
        }
    }

    const handleApagarJuncao = async (ucDistribuicaoId) => {
        const response = await apagarUcJuncao(id, ucDistribuicaoId)
        if (response.success) {
            const atualizado = await getUcsDistribuicao(id)
            if (atualizado.success) setAtribuicoes(atualizado.data)
        } else {
            alert(`Erro ao apagar junção`)
        }
    }

    const handleVerificar = async () => {
        const response = await getVerificarDistribuicao(id)
        if (response.success) {
            setResultadoVerificacao(response.data)
            setModalVerificacaoAberto(true)
        }
    }

    const atribuicoesFiltradas = useMemo(() =>
            atribuicoes.filter(l => {
                const matchAnoCurricular = !anoCurricularFiltro || String(l.ucAno) === anoCurricularFiltro
                const matchCurso = !cursoFiltro || String(l.cursoId) === cursoFiltro
                const matchDocente = !docenteFiltro ||
                    (l.docenteNome && l.docenteNome.toLowerCase().includes(docenteFiltro.toLowerCase()))
                const matchSemestre = !semestreFiltro || String(l.ucSemestre) === semestreFiltro
                return matchAnoCurricular && matchCurso && matchSemestre && matchDocente
            }),
        [atribuicoes, anoCurricularFiltro, cursoFiltro, docenteFiltro, semestreFiltro]
    )

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
                onCriarTurno={handleCriarTurno}
                onApagarTurno={handleApagarTurno}
                onJuncao={abrirJuncao}
                onApagarJuncao={handleApagarJuncao}
                onDuplicar={handleDuplicar}
            />

            <PainelDocentes docentes={docentes}/>
            {modalAberto && <ModalJuncao atribuicao = {atribuicaoSelecionada} atribuicoes={atribuicoes} onClose={() => setModalAberto(false)} onSubmit={handleJuncao}/>}
            <BarraInferior
                onVerificar={handleVerificar}
            />

            {modalVerificacaoAberto && resultadoVerificacao && (
                <ModalVerificacao
                    resultado={resultadoVerificacao}
                    onClose={() => setModalVerificacaoAberto(false)}
                />)}
        </SideBarCollapsed_TopBar>



    )
}

export default DistribuicaoPage