import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'
import FilterBar from '../components/FilterBar'
import TabelaDistribuicoes from '../components/TabelaDistribuicoes.jsx'
import ModalDistribuicao from '../components/ModalDistribuicao.jsx'
import {getDistribuicoes, criarDistribuicao, desativarDistribuicao} from '../api/distribuicoesApi'
import {UserContext} from "../UserContext.jsx";

export const DistribuicoesPage =() => {
    const { user } = useContext(UserContext)
    const [distribuicoes, setDistribuicoes] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [anoLetivo, setAnoLetivo] = useState('')
    const [estado, setEstado] = useState('')
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)


    useEffect(() => {
       // if (!user) return
        const carregar = async () => {
            const response = await getDistribuicoes()

            if (response.success) {
                setDistribuicoes(response.data)
            } else {
                setErro(response.message)
            }
        }
        carregar()
    }, [user])

    const handleCriar = async (dados) => {
        const response = await criarDistribuicao(dados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getDistribuicoes()
            if (atualizado.success) setDistribuicoes(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleDesativar = async (id) =>{

        const response = await desativarDistribuicao(id)
        if(response.success){
            const atualizado = await getDistribuicoes()
            if (atualizado.success) setDistribuicoes(atualizado.data)
        }


    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, anoLetivo, estado, porPagina])


    const distribuicoesFiltradas = distribuicoes.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        const matchAno = !anoLetivo || d.ano_letivo === anoLetivo
        const matchEstado = !estado || d.estado === estado
        return matchPesquisa && matchAno && matchEstado
    })


    const totalPaginas = Math.max(1, Math.ceil(distribuicoesFiltradas.length / porPagina))
    const distribuicoesPagina = distribuicoesFiltradas.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar >
            <PageHeader titulo="Distribuições de Serviço" subtitulo="Lista de todas as distribuições de serviço criadas" onCriar={()  =>  setModalAberto(true)} />
            <FilterBar pesquisa={pesquisa} setPesquisa={setPesquisa} anoLetivo={anoLetivo} setAnoLetivo={setAnoLetivo} estado={estado} setEstado={setEstado} onLimpar={() => { setPesquisa(''); setAnoLetivo(''); setEstado('') }} />
            <TabelaDistribuicoes
                distribuicoes={distribuicoesPagina}
                porPagina={porPagina}
                onDesativar={handleDesativar}
                onMudarPorPagina={setPorPagina}
                paginacao={{ inicio: (pagina - 1) * porPagina + 1, fim: Math.min(pagina * porPagina, distribuicoesFiltradas.length), total: distribuicoesFiltradas.length, atual: pagina, totalPaginas, onMudar: setPagina }}
            />
            {modalAberto && <ModalDistribuicao onClose={() => setModalAberto(false)} onSubmit={handleCriar} />}

        </SideBar_TopBar>
    )
}
