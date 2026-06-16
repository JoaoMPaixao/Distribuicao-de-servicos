import '../styles/estilos.css'

function PageHeader({ titulo, subtitulo, onCriar }) {
    return (
        <div className="page-header">
            <div>
                <h1>{titulo}</h1>
                <p>{subtitulo}</p>
            </div>
            <button className="btn-primary" onClick={onCriar}>
                <i className="bi bi-plus btn-icon"></i>
                Criar Distribuição
            </button>
        </div>
    )
}

export default PageHeader