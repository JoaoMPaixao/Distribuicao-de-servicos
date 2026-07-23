

function PageHeader({ titulo, subtitulo, onCriar, texto }) {
    return (
        <div className="page-header">
            <div>
                <h1>{titulo}</h1>
                <p>{subtitulo}</p>
            </div>
            <button className="btn-primary" onClick={onCriar}>
                <i className="bi bi-plus btn-icon"></i>
                {texto}
            </button>
        </div>
    )
}

export default PageHeader