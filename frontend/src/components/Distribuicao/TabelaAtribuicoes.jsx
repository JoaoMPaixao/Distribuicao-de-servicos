// TabelaDistribuicao.jsx
import ListarAtribuicoesCard from './ListarAtribuicoesCard.jsx'
import { useState } from "react";

function TabelaDistribuicao({ atribuicoes, onAtribuirDocente, onRemoverDocente, onAlterarHoras, onClonar }) {
    const [atribuicaoSelecionada, setAtribuicaoSelecionada] = useState(null)

    const handleDrop = (e, atribuicaoId) => {
        e.preventDefault()
        const docente = JSON.parse(e.dataTransfer.getData('docente'))
        onAtribuirDocente(atribuicaoId, docente)
    }

    const headerTextStyle = {
        display: 'flex',
        alignItems: 'flex-end',
        height: '64px',
        paddingBottom: '6px',
        fontSize: '10.5px',
        textTransform: 'uppercase',
        letterSpacing: '.04em'
    }

    return (
        <div className="tableArea">
            <table className="distTable">
                <thead>
                <tr>
                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Dep.</span></div>
                    </th>
                    <th rowSpan="2" style={{ padding: '0 12px', minWidth: '130px', textAlign: 'left' }}>
                        <div style={headerTextStyle}>Curso</div>
                    </th>
                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Ano</span></div>
                    </th>
                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Sem.</span></div>
                    </th>
                    <th rowSpan="2" style={{ padding: '0 12px', minWidth: '180px', textAlign: 'left' }}>
                        <div style={headerTextStyle}>Unidade Curricular</div>
                    </th>
                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Tipologia</span></div>
                    </th>
                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Horas</span></div>
                    </th>
                    <th rowSpan="2" style={{ padding: '0 12px', minWidth: '170px', textAlign: 'left' }}>
                        <div style={headerTextStyle}>Docente</div>
                    </th>

                    <th colSpan="3" className="thGroup accent">Horas/Semestre UC</th>

                    <th rowSpan="2" className="thRot">
                        <div className="rotWrap"><span>Nº semanas</span></div>
                    </th>

                    <th colSpan="2" className="thGroup accent">Horas/Semana UC</th>

                    <th colSpan="3" className="thGroup purple">Horas Totais/Semana Docente</th>

                    <th rowSpan="2" style={{ padding: '0 8px', minWidth: '70px' }}>
                        <div style={headerTextStyle}>Ações</div>
                    </th>
                </tr>
                <tr>
                    <th className="thLeaf">S1</th>
                    <th className="thLeaf">S2</th>
                    <th className="thLeaf">Média</th>
                    <th className="thLeaf">S1</th>
                    <th className="thLeaf">S2</th>
                    <th className="thLeaf" style={{ background: '#f5f3ff' }}>S1</th>
                    <th className="thLeaf" style={{ background: '#f5f3ff' }}>S2</th>
                    <th className="thLeaf" style={{ background: '#f5f3ff' }}>Média</th>
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
                        onRemoverDocente={() => onRemoverDocente(a.id)}
                        onAlterarHoras={(h) => onAlterarHoras(a.id, h)}
                        onClonar={() => onClonar(a.id)}
                    />
                ))}
                </tbody>
            </table>
        </div>
    )
}

export default TabelaDistribuicao