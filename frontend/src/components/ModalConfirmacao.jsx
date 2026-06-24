function ModalConfirmacao({ mensagem, onConfirmar, onCancelar }) {
    return (
        <div className="modal-overlay">     
            <div className="modal-confirmacao">
                <div className="modal-confirmacao-content">

                    <div className="modal-confirmacao-icon">
                        <i className="bi bi-exclamation-triangle"></i>
                    </div>
                    <h3>Confirmar Ação</h3>
                    <p>{mensagem}</p>
                    <div className="modal-confirmacao-actions">
                        <button className="btn-confirmacao-cancelar" onClick={onCancelar}>
                            Cancelar
                        </button>
                        <button className="btn-confirmacao-perigo" onClick={onConfirmar}>
                            <i className="bi bi-trash"></i> Desativar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default ModalConfirmacao;