function BarraInferior({ alteracoesPorGuardar, onGuardar, onSubmeter }) {
    return (
        <div className="bottom-bar">
            <div className="bottom-bar-left">
                <button className="btn-save" onClick={onGuardar}>
                    <i className="bi bi-floppy"></i> Guardar Alterações
                </button>
                {alteracoesPorGuardar && (
                    <span className="unsaved-dot visible" title="Alterações por guardar"></span>
                )}
            </div>
            <div className="bottom-bar-right">
                <button className="btn-submit" onClick={onSubmeter}>
                    <i className="bi bi-send"></i> Submeter Distribuição
                </button>
            </div>
        </div>
    )
}

export default BarraInferior