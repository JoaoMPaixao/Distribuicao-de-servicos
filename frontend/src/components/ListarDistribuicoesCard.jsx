import {useState} from "react";
import ModalConfirmacao from "./ModalConfirmacao.jsx";

const badgeClasses = {
    FINAL: 'badge-final',
    ABERTO: 'badge-aberto',
    FECHADO: 'badge-fechado',
    APROVADO: 'badge-aprovado',
    RECUSADO: 'badge-recusado',
}

function ListarDistribuicoesCard({ distribuicao, onVer, onComparar, onClonar, onDesativar }) {
    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

    return (
        <tr>
            <td className="cell-name">
                <div className="title">{distribuicao.nome}</div>
                <span className="version-tag">{distribuicao.versao}</span>
            </td>
            <td>
        <span className="cell-description" title={distribuicao.descricao}>
          {distribuicao.descricao}
        </span>
            </td>
            <td>{distribuicao.ano_letivo}</td>
            <td>
        <span className={`badge ${badgeClasses[distribuicao.estado] ?? ''}`}>
          {distribuicao.estado}
        </span>
            </td>
            <td className="date-cell">
                {distribuicao.data_criacao}

            </td>
            <td className="actions-cell">
                <button className="icon-btn" title="Ver/Editar" onClick={() => onVer(distribuicao.id)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
                <button className="icon-btn" title="Comparar" onClick={() => onComparar(distribuicao.id)}>
                    <i className="bi bi-arrow-down-up icon-icon"></i>
                </button>
                <button className="icon-btn" title="Clonar" onClick={() => onClonar(distribuicao.id)}>
                    <i className="bi bi-copy icon-icon"></i>
                </button>
                <button className="icon-btn" title="Desativar" onClick={() => setConfirmarDesativar(true)}>
                    <i className="bi bi-trash icon-icon"></i>
                </button>
                {confirmarDesativar && (
                    <ModalConfirmacao
                        mensagem={`Tem a certeza que deseja desativar "${distribuicao.nome}"?`}
                        onConfirmar={() => { onDesativar(distribuicao.id); setConfirmarDesativar(false) }}
                        onCancelar={() => setConfirmarDesativar(false)}
                    />
                )}
            </td>

        </tr>

    )
}

export default ListarDistribuicoesCard