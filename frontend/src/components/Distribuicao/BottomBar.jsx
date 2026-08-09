function BarraInferior({onVerificar}) {
    return (
        <div className={`bottom-bar`}>
            <div className="bottom-bar-right">
                <button className="btn-save" onClick={onVerificar}>
                    <i className="bi bi-clipboard-check"></i> Verificar
                </button>
            </div>
        </div>
    )
}

export default BarraInferior