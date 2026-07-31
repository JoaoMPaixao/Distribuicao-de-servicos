import { useState } from 'react'

function ModalJuncao({ atribuicao, atribuicoes, onClose, onSubmit }) {
    const [selecionados, setSelecionados] = useState([])
    const [pesquisa, setPesquisa] = useState('')

    const opcoes = atribuicoes.filter(l =>
        l.id !== atribuicao?.id && l.ucTipoAulaId != null
    )

    const opcoesFiltradas = opcoes.filter(l =>
        l.ucNome?.toLowerCase().includes(pesquisa.toLowerCase()) ||
        l.ucTipoNome?.toLowerCase().includes(pesquisa.toLowerCase())
    )

    const toggleSelecionado = (a) => {
        setSelecionados(prev =>
            prev.find(s => s.id === a.id)
                ? prev.filter(s => s.id !== a.id)
                : [...prev, a]
        )
    }

    const handleConfirmar = () => {
        if (selecionados.length === 0) return
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

                {/* barra de pesquisa */}
                <div className="panel-search-wrap" style={{ marginBottom: '10px' }}>
                    <i className="bi bi-search" style={{
                        position: 'absolute', left: '9px', top: '50%',
                        transform: 'translateY(-50%)', fontSize: '13px',
                        color: 'var(--muted)', pointerEvents: 'none'
                    }}></i>
                    <input
                        className="panel-search-input"
                        type="text"
                        placeholder="Pesquisar UC..."
                        value={pesquisa}
                        onChange={(e) => setPesquisa(e.target.value)}
                        autoFocus
                    />
                </div>

                <div style={{ maxHeight: '260px', overflowY: 'auto' }}>
                    {opcoesFiltradas.length === 0 ? (
                        <p style={{ fontSize: '13px', color: 'var(--muted)', padding: '8px 0' }}>
                            {pesquisa ? 'Nenhuma UC encontrada.' : 'Não existem outras linhas disponíveis.'}
                        </p>
                    ) : (
                        opcoesFiltradas.map((l) => {
                            const estaSelecionado = selecionados.find(s => s.id === l.id)
                            return (
                                <div
                                    key={l.id}
                                    className={`turno-option ${estaSelecionado ? 'selected' : ''}`}
                                    onClick={() => toggleSelecionado(l)}
                                >
                                    <i className={`bi ${estaSelecionado ? 'bi-check-square' : 'bi-square'}`}></i>
                                    {l.ucNome} — {l.ucTipoNome}
                                    {l.turno && (
                                        <span style={{ color: 'var(--muted)', fontSize: '11px' }}>
                                            {' '}(Turno {l.turno})
                                        </span>
                                    )}
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