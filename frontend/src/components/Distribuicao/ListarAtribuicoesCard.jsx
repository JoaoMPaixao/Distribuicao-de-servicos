// LinhaDistribuicao.jsx
import { useState } from 'react'
import ModalJuncao from './ModalJuncao'

    function ListarAtribuicoesCard({ atribuicao, selecionada, onSelecionar, onDrop, onRetirarDocente, onAlterarHoras, onClonar, onJuncao }) {
    const [dragOver, setDragOver] = useState(false)
    const [modalJuncaoAberto, setModalJuncaoAberto] = useState(false)

    return (
        <>
            <tr
                className={selecionada ? 'selected' : ''}
                onClick={onSelecionar}
            >
                <td className="center mono">{atribuicao.ucDepartamentoNome}</td>
                <td className="center mono">{atribuicao.cursoNome}</td>
                <td className="center mono">{atribuicao.ucAno}</td>
                <td className="center mono">{atribuicao.ucSemestre
                }</td>
                <td className="center mono">{atribuicao.ucNome
                }</td>
                <td className="center"><span className="badge-tipo">{atribuicao.ucTipoNome}</span></td>
                <td className="center mono">{atribuicao.turno}</td>
                <td className="center">
                    <input
                        className="horas-input-td"
                        type="number"
                        value={atribuicao.horasAtribuidas}
                        min="0" max={atribuicao.ucTipoHoras}
                        onClick={(e) => e.stopPropagation()}
                        onChange={(e) => onAlterarHoras(Number(e.target.value))}
                    />
                </td>


                <td>
                    <div
                        className={`drop-zone ${dragOver ? 'over' : ''}`}
                        onDragOver={(e) => { e.preventDefault(); setDragOver(true) }}
                        onDragLeave={() => setDragOver(false)}
                        onDrop={(e) => { setDragOver(false); onDrop(e) }}
                    >
                        {atribuicao.docenteId ? (
                            <span className="assigned-chip" onClick={(e) => { e.stopPropagation(); onRetirarDocente() }}>
                <i className="bi bi-x" style={{ fontSize: '10px' }}></i> {atribuicao.docenteNome}
              </span>
                        ) : (
                            <span>Soltar aqui</span>
                        )}
                    </div>
                </td>

                {/* Colunas calculadas */}
                <td className="td-computed">{atribuicao.horasSemestreS1}</td>
                <td className="td-computed">{atribuicao.horasSemestreS2}</td>
                <td className="td-computed bold">{atribuicao.horasSemestreMedia}</td>
                <td className="td-computed">{atribuicao.nSemanas}</td>
                <td className="td-computed">{atribuicao.horasSemanaS1}</td>
                <td className="td-computed">{atribuicao.horasSemanaS2}</td>
                <td className="td-computed" style={{ background: '#faf8ff' }}>{atribuicao.horasDocenteS1}</td>
                <td className="td-computed" style={{ background: '#faf8ff' }}>{atribuicao.horasDocenteS2}</td>
                <td className="td-computed bold" style={{ background: '#faf8ff' }}>
                    {atribuicao.horasDocenteMedia}
                </td>

                {/* Ações */}
                <td onClick={(e) => e.stopPropagation()}>
                    <div className="actions-cell">
                        <button className="icon-btn juncao" title="Criar junção"
                                onClick={() => onJuncao(atribuicao)}>
                            <i className="bi bi-link-45deg"></i>
                        </button>
                        <button className="icon-btn" title="Criar turno" onClick={onClonar}>
                            <i className="bi bi-copy"></i>
                        </button>
                    </div>
                </td>
            </tr>
        </>
    )
}

export default ListarAtribuicoesCard