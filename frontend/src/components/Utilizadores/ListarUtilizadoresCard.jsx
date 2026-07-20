import { useState } from "react";
import ModalConfirmacao from "../ModalConfirmacao.jsx";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarUtilizadoresCard({ utilizador,  onAlterar, onApagar }) {

    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

    return (
        <tr>
            <td className="cell-name">
                <div className="title">{utilizador.nome}</div>
            </td>
            <td>
                <h6>{utilizador.email}</h6>
            </td>
            <td>
                <span className="cell-description" title={utilizador.password}>
                    {utilizador.password}
                </span>
            </td>
            <td>
                <span className={`badge ${badgeClasses[utilizador.estado]}`}>
                    {utilizador.estado}
                </span>
            </td>
            <td className="cell-name">{utilizador.departamento_nome}</td>
            <td className="cell-name">{utilizador.cargo_tipo}</td>
            <td className="actions-cell">
                <button className="icon-btn" title="Editar" onClick={() => onAlterar(utilizador)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
                <button className="icon-btn" title="Apagar" onClick={() => setConfirmarDesativar(true)}>
                    <i className="bi bi-trash icon-icon"></i>
                </button>
                {confirmarDesativar && (
                    <ModalConfirmacao
                        mensagem={`Tem a certeza que deseja apagar "${utilizador.nome}"?`}
                        onConfirmar={() => { onApagar(utilizador.id); setConfirmarDesativar(false) }}
                        onCancelar={() => setConfirmarDesativar(false)}
                    />
                )}
            </td>
        </tr>
    )
}

export default ListarUtilizadoresCard