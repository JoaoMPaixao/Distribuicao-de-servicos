import { useState } from "react";
import ModalConfirmacao from "../ModalConfirmacao.jsx";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarDocentesCard({ docente,  onAlterar, onAbrirContrato, onApagar}) {

    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

    return (
        <tr>
            <td className="cell-name">
                <div className="title">{docente.nome}</div>
            </td>
            <td>
                <span className="cell-description" title={docente.email}>
                    {docente.email}
                </span>
            </td>
            <td>
                <span className={`badge ${badgeClasses[docente.estado]}`}>
                    {docente.estado}
                </span>
            </td>
            <td className="cell-name">{docente.departamentoNome}</td>
            <td className="cell-name">{docente.contratoDataInicio}</td>
            <td className="cell-name">{docente.contratoDataFim}</td>
            <td className="cell-name">{docente.contratoDescricao}</td>
            <td>
                <span className="cell-description" title={docente.contratoRelacaoJuridica}>
                    {docente.contratoRelacaoJuridica}
                </span>
            </td>
            <td>
                <span className="cell-description" title={docente.contratoCategoria}>
                    {docente.contratoCategoria}
                </span>
            </td>
            <td className="cell-name">{docente.contratoDedicacao}</td>
            <td className="actions-cell">
                <button className="icon-btn" title="Editar" onClick={() => onAlterar(docente)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
                <button className="icon-btn" title="Novo Contrato"
                        onClick={() => onAbrirContrato(docente, false)}>
                    <i className="bi bi-file-plus icon-icon"></i>
                </button>

                {/* só aparece se já tiver contrato */}
                {docente.contratoId && (
                    <button className="icon-btn" title="Editar Contrato Atual"
                            onClick={() => onAbrirContrato(docente, true)}>
                        <i className="bi bi-file-text icon-icon"></i>
                    </button>
                )}
                <button className="icon-btn" title="Apagar" onClick={() => setConfirmarDesativar(true)}>
                    <i className="bi bi-trash icon-icon"></i>
                </button>
                {confirmarDesativar && (
                    <ModalConfirmacao
                        mensagem={`Tem a certeza que deseja apagar "${docente.nome}"?`}
                        onConfirmar={() => { onApagar(docente.id); setConfirmarDesativar(false) }}
                        onCancelar={() => setConfirmarDesativar(false)}
                    />
                )}
            </td>

        </tr>
    )
}

export default ListarDocentesCard