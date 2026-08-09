import React from 'react'

function ModalVerificacao({ resultado, onClose }) {
    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal" role="dialog">
                <div className="modal-header">
                    <div className="modal-header-left">
                        <div className="modal-icon">
                            <i className={`bi ${resultado.valida ? 'bi-check-circle' : 'bi-exclamation-triangle'}`} />
                        </div>
                        <div>
                            <h2 className="modal-title">
                                {resultado.valida ? 'Distribuição válida' : 'Problemas encontrados'}
                            </h2>
                            <p className="modal-subtitle">
                                {resultado.valida
                                    ? 'Todas as UCs têm docente e horas atribuídas.'
                                    : `${resultado.problemas.length} problema(s) encontrado(s).`}
                            </p>
                        </div>
                    </div>
                    <button className="modal-close" onClick={onClose} aria-label="Fechar">
                        <i className="bi bi-x"></i>
                    </button>
                </div>

                <div className="modal-body">
                    {/* Resumo de métricas */}
                    <div className="modal-summary-grid">
                        <div className={`modal-summary-card ${resultado.ucsSemDocente > 0 ? 'has-error' : ''}`}>
                            <div className="modal-summary-value">{resultado.ucsSemDocente}</div>
                            <div className="modal-summary-label">Sem docente</div>
                        </div>
                        <div className={`modal-summary-card ${resultado.ucsSemHoras > 0 ? 'has-error' : ''}`}>
                            <div className="modal-summary-value">{resultado.ucsSemHoras}</div>
                            <div className="modal-summary-label">Horas insuficientes</div>
                        </div>
                    </div>

                    {/* Lista de problemas */}
                    {resultado.problemas.length > 0 && (
                        <div className="modal-problems-list">
                            {resultado.problemas.map((p, i) => (
                                <div key={i} className="modal-problem-item">
                                    <i className="bi bi-exclamation-circle modal-problem-icon"></i>
                                    <span>{p}</span>
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Fechar</button>
                </div>
            </div>
        </div>
    )
}

export default ModalVerificacao