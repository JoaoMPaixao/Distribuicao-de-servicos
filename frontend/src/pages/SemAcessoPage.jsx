import { useNavigate } from 'react-router-dom'

function SemAcessoPage() {
    const navigate = useNavigate()

    return (
        <div className={"login-panel"}
        >
            <div className={"login-card"} style={{textAlign: "center"}}>
                <i className="bi bi-shield-x" style={{fontSize: "50px"}}></i>
                <h1>Sem permissão</h1>
                <p>
                    Não tem acesso a esta página.
                </p>
                <button className="btn-primary" onClick={() => navigate(-1)}>
                    <i className="bi bi-arrow-left btn-icon"></i>
                    Voltar
                </button>


            </div>

        </div>
    )
}

export default SemAcessoPage