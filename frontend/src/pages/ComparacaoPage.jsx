// pages/ComparacaoPage.jsx
import { useState, useEffect } from 'react'
import { getDistribuicoes } from '../api/distribuicoesApi'
import { compararDistribuicoes } from '../api/distribuicoesApi'
import SideBar_TopBar from "../layouts/SideBar_TopBar.jsx";

function ComparacaoPage() {
    const [distribuicoes, setDistribuicoes] = useState([])
    const [dist1Id, setDist1Id] = useState('')
    const [dist2Id, setDist2Id] = useState('')
    const [resultado, setResultado] = useState(null)
    const [apenasdiferencas, setApenasDiferencas] = useState(false)

    useEffect(() => {
        getDistribuicoes().then(r => { if (r.success) setDistribuicoes(r.data) })
    }, [])

    const handleComparar = async () => {
        if (!dist1Id || !dist2Id) return
        const response = await compararDistribuicoes(dist1Id, dist2Id)

        if (response.success) setResultado(response.data)
        else{

        }
    }

    const linhas = resultado?.linhas ?? []
    const linhasFiltradas = apenasdiferencas ? linhas.filter(l => l.temDiferenca) : linhas

    return (
        <SideBar_TopBar >
            <div className="page-header">
                <div>
                    <h1>Comparação de Distribuições</h1>
                    <p>Compara duas distribuições lado a lado</p>
                </div>
            </div>

            {/* seleção das distribuições */}
            <div className="filter-bar">
                <div className="select-wrap">
                    <label>Distribuição A</label>
                    <select value={dist1Id} onChange={(e) => setDist1Id(e.target.value)}>
                        <option value="">Selecionar...</option>
                        {distribuicoes.map(d => (
                            <option key={d.id} value={d.id}>{d.nome} - v{d.versao}</option>
                        ))}
                    </select>
                </div>
                <div className="select-wrap">
                    <label>Distribuição B</label>
                    <select value={dist2Id} onChange={(e) => setDist2Id(e.target.value)}>
                        <option value="">Selecionar...</option>
                        {distribuicoes.filter(d => String(d.id) !== dist1Id).map(d => (
                            <option key={d.id} value={d.id}>{d.nome} - v{d.versao}</option>
                        ))}
                    </select>
                </div>
                <button className="btn-primary" onClick={handleComparar} disabled={!dist1Id || !dist2Id}>
                    <i className="bi bi-arrow-left-right btn-icon"></i>
                    Comparar
                </button>
                {resultado && (
                    <label style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '13px', cursor: 'pointer' }}>
                        <input type="checkbox" checked={apenasdiferencas} onChange={(e) => setApenasDiferencas(e.target.checked)} />
                        Mostrar só diferenças ({resultado.totalDiferencas})
                    </label>
                )}
            </div>

            {resultado && (
                <div className="table-card">
                    <div className="table-wrap">
                        <table>
                            <thead>
                            <tr>
                                <th>Unidade Curricular</th>
                                <th>Tipologia</th>
                                <th>Turno</th>
                                <th style={{ background: '#eff6ff' }}>{resultado.nomeDistribuicao1} - Docente</th>
                                <th style={{ background: '#eff6ff' }}>{resultado.nomeDistribuicao1} - Horas</th>
                                <th style={{ background: '#f5f3ff' }}>{resultado.nomeDistribuicao2} - Docente</th>
                                <th style={{ background: '#f5f3ff' }}>{resultado.nomeDistribuicao2} - Horas</th>
                            </tr>
                            </thead>
                            <tbody>
                            {linhasFiltradas.map((l, i) => (
                                <tr key={i} style={{ background: l.temDiferenca ? '#fefce8' : 'transparent' }}>
                                    <td style={{ fontWeight: 600 }}>{l.ucNome}</td>
                                    <td><span className="badge-tipo">{l.ucTipoNome}</span></td>
                                    <td className="center">{l.turno}</td>
                                    <td style={{ background: !l.docenteIgual ? '#fee2e2' : '#eff6ff', color: !l.docenteIgual ? '#991b1b' : 'inherit' }}>
                                        {l.docenteNome1 ?? '—'}
                                    </td>
                                    <td style={{ background: !l.horasIguais ? '#fee2e2' : '#eff6ff', textAlign: 'center' }}>
                                        {l.horasAtribuidas1}h
                                    </td>
                                    <td style={{ background: !l.docenteIgual ? '#fee2e2' : '#f5f3ff', color: !l.docenteIgual ? '#991b1b' : 'inherit' }}>
                                        {l.docenteNome2 ?? '—'}
                                    </td>
                                    <td style={{ background: !l.horasIguais ? '#fee2e2' : '#f5f3ff', textAlign: 'center' }}>
                                        {l.horasAtribuidas2}h
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </SideBar_TopBar>
    )
}

export default ComparacaoPage