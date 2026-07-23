// PainelDocentes.jsx
import { useState } from 'react'
import DocItem from './DocItem'

function PainelDocentes({ docentes }) {
    const [pesquisa, setPesquisa] = useState('')

    const docentesFiltrados = docentes.filter(d =>
        d.nome.toLowerCase().includes(pesquisa.toLowerCase())
    )

    return (
        <div className="docentes-panel">
            <div style={{ padding: '12px 12px 0', borderBottom: '1px solid var(--border)' }}>
                <p style={{ fontSize: '11px', fontWeight: 600, textTransform: 'uppercase', letterSpacing: '.06em', color: 'var(--muted)', paddingBottom: '10px' }}>
                    Docentes
                </p>
                <div className="panel-search-wrap" style={{ marginBottom: '10px' }}>
                    <i className="bi bi-search"></i>
                    <input
                        className="panel-search-input"
                        type="text"
                        placeholder="Pesquisar docente…"
                        value={pesquisa}
                        onChange={(e) => setPesquisa(e.target.value)}
                    />
                </div>
            </div>
            <div className="panel-body">
                <div className="panel-list">
                    <div className="panel-list-label">Docentes disponíveis</div>
                    {docentesFiltrados.map(d => (
                        <DocItem key={d.id} docente={d} />
                    ))}
                </div>
            </div>
        </div>
    )
}

export default PainelDocentes