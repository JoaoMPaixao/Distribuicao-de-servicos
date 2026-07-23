// LinhaDistribuicao.jsx
import { useState } from 'react'
import ModalJuncao from './ModalJuncao'

    function ListarAtribuicoesCard({ atribuicao, selecionada, onSelecionar, onDrop, onRemoverDocente, onAlterarHoras, onClonar }) {
    const [dragOver, setDragOver] = useState(false)
    const [modalJuncaoAberto, setModalJuncaoAberto] = useState(false)

    return (
        <>
            <tr
                className={selecionada ? 'selected' : ''}
                onClick={onSelecionar}
            >
                <td className="center mono">{atribuicao.cursoDepartamentoNome}</td>
                <td className="td-curso">{atribuicao.cursoNome}</td>
                <td className="center mono">{atribuicao.ucAno}</td>
                <td className="center mono">{atribuicao.ucSemestre
                }</td>
                <td className="td-uc">{atribuicao.ucNome
                }</td>
                <td className="center"><span className="badge-tipo">{atribuicao.ucTipoNome}</span></td>
                <td className="center">
                    <input
                        className="horas-input-td"
                        type="number"
                        value={atribuicao.horasAtribuidas}
                        min="0.5" max="20"
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
                        {atribuicao.docente ? (
                            <span className="assigned-chip" onClick={(e) => { e.stopPropagation(); onRemoverDocente() }}>
                <i className="bi bi-x" style={{ fontSize: '10px' }}></i> {atribuicao.docente.nome}
              </span>
                        ) : (
                            <span className="placeholder">Soltar aqui</span>
                        )}
                    </div>
                </td>

                {/* Colunas calculadas */}
                <td className="td-computed">{'—'}</td>
                <td className="td-computed">{'—'}</td>
                <td className="td-computed bold">-</td>
                <td className="td-computed">-</td>
                <td className="td-computed">-</td>
                <td className="td-computed">-</td>
                <td className="td-computed" style={{ background: '#faf8ff' }}>-</td>
                <td className="td-computed" style={{ background: '#faf8ff' }}>-</td>
                <td className="td-computed bold" style={{ background: '#faf8ff' }}>
                   -
                </td>

                {/* Ações */}
                <td onClick={(e) => e.stopPropagation()}>
                    <div className="actions-cell">
                        <button className="icon-btn juncao" title="Criar junção"
                                onClick={() => setModalJuncaoAberto(true)}>
                            <i className="bi bi-link-45deg"></i>
                        </button>
                        <button className="icon-btn" title="Criar turno" onClick={onClonar}>
                            <i className="bi bi-copy"></i>
                        </button>
                    </div>
                </td>
            </tr>

            {modalJuncaoAberto && (
                <ModalJuncao
                    label={`${atribuicao.uc_nome} — ${atribuicao.tipo}`}
                    onClose={() => setModalJuncaoAberto(false)}
                    onConfirmar={(target) => {
                        console.log('junção criada com', target)
                        setModalJuncaoAberto(false)
                    }}
                />
            )}
        </>
    )
}

export default ListarAtribuicoesCard