// TabelaDistribuicao.jsx
import ListarAtribuicoesCard from './ListarAtribuicoesCard.jsx'
import { useState } from "react";

function TabelaDistribuicao({ atribuicoes, onAtribuirDocente, onRetirarDocente, onAlterarHoras, onCriarTurno, onApagarTurno, onJuncao, onApagarJuncao, onDuplicar }) {
    const [atribuicaoSelecionada, setAtribuicaoSelecionada] = useState(null)

    const handleDrop = (e, atribuicaoId) => {
        e.preventDefault()
        const docente = JSON.parse(e.dataTransfer.getData('docente'))
        onAtribuirDocente(atribuicaoId, docente)
    }

    return (
        <div className="table-area">
            <table className="dist-table">
                <thead>
                <tr>
                    <th rowSpan="2" >
                        <div className="th-header"><span>Dep.</span></div>
                    </th>
                    <th rowSpan="5">
                        <div className="th-header">Curso</div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Ano</span></div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Sem.</span></div>
                    </th>
                    <th rowSpan="2" >
                        <div className="th-header">Unidade Curricular</div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Tipologia</span></div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>UcHoras</span></div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Turno</span></div>
                    </th>
                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Horas</span></div>
                    </th>
                    <th rowSpan="2" style={{ padding: '0 12px', minWidth: '170px', textAlign: 'left' }}>
                        <div className="th-header">Docente</div>
                    </th>

                    <th colSpan="3" className="th-group accent">Horas/Semestre UC</th>

                    <th rowSpan="2" className="th-rot">
                        <div className="rot-wrap"><span>Nº semanas</span></div>
                    </th>

                    <th colSpan="2" className="th-group accent">Horas/Semana UC</th>

                    <th colSpan="3" className="th-group purple">Horas Totais/Semana Docente</th>

                    <th rowSpan="2" style={{ padding: '0 8px', minWidth: '70px' }}>
                        <div className="th-header">Ações</div>
                    </th>
                </tr>
                <tr>
                    <th className="th-leaf">S1</th>
                    <th className="th-leaf">S2</th>
                    <th className="th-leaf">Média</th>
                    <th className="th-leaf">S1</th>
                    <th className="th-leaf">S2</th>
                    <th className="th-leaf" style={{ background: '#f5f3ff' }}>S1</th>
                    <th className="th-leaf" style={{ background: '#f5f3ff' }}>S2</th>
                    <th className="th-leaf" style={{ background: '#f5f3ff' }}>Média</th>
                </tr>
                </thead>
                <tbody>
                {atribuicoes.map(a => (
                    <ListarAtribuicoesCard
                        key={a.id}
                        atribuicao={a}
                        selecionada={atribuicaoSelecionada === a.id}
                        onSelecionar={() => setAtribuicaoSelecionada(a.id)}
                        onDrop={(e) => handleDrop(e, a.id)}
                        onRetirarDocente={() => onRetirarDocente(a.id)}
                        onAlterarHoras={(h) => onAlterarHoras(a.id, h)}
                        onCriarTurno={() => onCriarTurno(a.id)}
                        onApagarTurno={onApagarTurno}
                        onJuncao={onJuncao}
                        onApagarJuncao={onApagarJuncao}
                        onDuplicar={() => onDuplicar(a.id)}
                    />
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default TabelaDistribuicao