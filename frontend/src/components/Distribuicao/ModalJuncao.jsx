import { useState } from 'react'

function ModalJuncao({ atribuicao, atribuicoes, onClose, onSubmit }) {
    const [selecionados, setSelecionados] = useState([])

    // filtra a própria atribuição e as que não têm ucTipoAulaId (já são junções)
    const opcoes = atribuicoes.filter(l =>
        l.id !== atribuicao?.id && l.ucTipoAulaId != null
    )

    const toggleSelecionado = (a) => {
        setSelecionados(prev =>
            prev.find(s => s.id === a.id)
                ? prev.filter(s => s.id !== a.id)  // remove se já estava
                : [...prev, a]                      // adiciona se não estava
        )
    }

    const handleConfirmar = () => {
        if (selecionados.length === 0) return

        // junta o ucTipoAulaId da atribuição de origem com os das selecionadas
        const ucTipoAulaIds = [
            atribuicao.ucTipoAulaId,
            ...selecionados.map(s => s.ucTipoAulaId)
        ]

        onSubmit(ucTipoAulaIds)
        onClose()
    }

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    return (
        <div className="modal-backdrop open" onClick={handleOverlayClick}>
            <div className="modal-box">
                <div className="modal-title">
                    Juntar "{atribuicao?.ucNome} — {atribuicao?.ucTipoNome}" com…
                </div>
                <div className="modal-sub">
                    Seleciona uma ou mais linhas para criar uma junção
                </div>

                <div>
                    {opcoes.length === 0 ? (
                        <p style={{ fontSize: '13px', color: 'var(--muted)', padding: '8px 0' }}>
                            Não existem outras linhas disponíveis.
                        </p>
                    ) : (
                        opcoes.map((l) => {
                            const estaSelecionado = selecionados.find(s => s.id === l.id)
                            return (
                                <div
                                    key={l.id}
                                    className={`turno-option ${estaSelecionado ? 'selected' : ''}`}
                                    onClick={() => toggleSelecionado(l)}
                                >
                                    <i className={`bi ${estaSelecionado ? 'bi-check-square' : 'bi-square'}`}></i>
                                    {l.ucNome} — {l.ucTipoNome}
                                    {l.turno && <span style={{ color: 'var(--muted)', fontSize: '11px' }}> (Turno {l.turno})</span>}
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
                        disabled={selecionados.length === 0}
                    >
                        Criar junção {selecionados.length > 0 && `(${selecionados.length + 1})`}
                    </button>
                </div>
            </div>
        </div>
    )
}

export default ModalJuncao