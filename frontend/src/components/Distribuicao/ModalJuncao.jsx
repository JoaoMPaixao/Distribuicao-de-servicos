// ModalJuncao.jsx
import { useState } from 'react'

function ModalJuncao({ label, linhas, onClose, onConfirmar }) {
    const [selecionado, setSelecionado] = useState(null)

    // filtra a própria linha para não aparecer como opção
    const opcoes = linhas.filter(l => `${l.uc} — ${l.tipo}` !== label)

    const handleConfirmar = () => {
        if (!selecionado) return
        onConfirmar(selecionado)
    }

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    return (
        <div className="modal-backdrop open" onClick={handleOverlayClick}>
            <div className="modal-box">

                <div className="modal-title">Juntar "{label}" com…</div>
                <div className="modal-sub">Seleciona a linha com a qual queres criar uma junção</div>

                <div>
                    {opcoes.length === 0 ? (
                        <p style={{ fontSize: '13px', color: 'var(--muted)', padding: '8px 0' }}>
                            Não existem outras linhas disponíveis para junção.
                        </p>
                    ) : (
                        opcoes.map((l) => {
                            const opcaoLabel = `${l.uc} — ${l.tipo}`
                            return (
                                <div
                                    key={l.id}
                                    className={`turno-option ${selecionado?.id === l.id ? 'selected' : ''}`}
                                    onClick={() => setSelecionado(l)}
                                >
                                    <i className="bi bi-layout-text-sidebar-reverse"></i>
                                    {opcaoLabel}
                                </div>
                            )
                        })
                    )}
                </div>

                <div className="modal-actions">
                    <button className="btn-cancel" onClick={onClose}>Cancelar</button>
                    <button
                        className="btn-confirm"
                        onClick={handleConfirmar}
                        disabled={!selecionado}
                    >
                        Criar junção
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalJuncao