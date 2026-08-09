import {useState, useEffect, useContext} from 'react'
import SideBar_TopBar from '../layouts/SideBar_TopBar.jsx'
import PageHeader from '../components/PageHeader'
import {
    getDepartamentos,
    criarDepartamento,
    alterarDepartamentos, apagarDepartamento
} from '../api/departamentosApi.jsx'
import {UserContext} from "../UserContext.jsx";
import TabelaDepartamentos from "../components/Departamentos/TabelaDepartamentos.jsx";
import FilterBarDepartamentos from "../components/Departamentos/FilterBarDepartamentos.jsx";
import ModalDepartamentos from "../components/Departamentos/ModalDepartamentos.jsx";



export const DepartamentosPage = () => {
    const {user} = useContext(UserContext)
    const [departamentos, setDepartamentos] = useState([])
    const [pesquisa, setPesquisa] = useState('')
    const [pagina, setPagina] = useState(1)
    const [porPagina, setPorPagina] = useState(5)
    const [erro, setErro] = useState(null)
    const [modalAberto, setModalAberto] = useState(false)
    const [erroModal, setErroModal] = useState(null)
    const [departamentoSelecionado, setDepartamentoSelecionado] = useState(null)


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
        setDepartamentoSelecionado(null)
        setModalAberto(true)
    }

    const abrirEditar = (departamento) => {
        setDepartamentoSelecionado(departamento)
        setModalAberto(true)
    }

    const handleSubmit = async (idOuDados, dadosSeEdicao) => {

        const response = departamentoSelecionado
            ? await alterarDepartamentos(idOuDados, dadosSeEdicao)
            : await criarDepartamento(idOuDados)

        if (response.success) {
            setModalAberto(false)
            const atualizado = await getDepartamentos()
            if (atualizado.success) setDepartamentos(atualizado.data)
        } else {
            setErroModal(response.message)
        }
    }

    const handleApagar = async (id) =>{

        const response = await apagarDepartamento(id)

        if(response.success){

            const atualizado = await getDepartamentos()
            if (atualizado.success) setDepartamentos(atualizado.data)
        } else {
            setErroModal(response.message)
        }

    }

    //atualiza a pagina sem dar reset para os filtros
    useEffect(() => {
        setPagina(1)
    }, [pesquisa, porPagina])


    const departamentosFiltrados = departamentos.filter((d) => {
        const matchPesquisa = d.nome.toLowerCase().includes(pesquisa.toLowerCase())
        return matchPesquisa
    })


    const totalPaginas = Math.max(1, Math.ceil(departamentosFiltrados.length / porPagina))
    const departamentosPagina = departamentosFiltrados.slice(
        (pagina - 1) * porPagina,
        pagina * porPagina
    )


    if (erro) return <p>Erro: {erro}</p>
    if (erroModal) return <p>ErroModal: {erroModal}</p>

    return (
        <SideBar_TopBar>
            <PageHeader titulo="Gestão de departamentos" subtitulo="Lista de todos os departamentos"
                        onCriar={abrirCriar} texto="Criar Departamento"/>
            <FilterBarDepartamentos pesquisa={pesquisa} setPesquisa={setPesquisa} />
            <TabelaDepartamentos
                departamentos={departamentosPagina}
                porPagina={porPagina}
                onMudarPorPagina={setPorPagina}
                paginacao={{
                    inicio: (pagina - 1) * porPagina + 1,
                    fim: Math.min(pagina * porPagina, departamentosFiltrados.length),
                    total: departamentos.length,
                    atual: pagina,
                    totalPaginas,
                    onMudar: setPagina
                }}
                onAlterar={abrirEditar}
                onApagar={handleApagar}
            />
            {modalAberto && <ModalDepartamentos departamento= {departamentoSelecionado} onClose={() => setModalAberto(false)} onSubmit={handleSubmit}/>}
        </SideBar_TopBar>
    )
}
