import { useState } from "react";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarUtilizadoresCard({ utilizador,  onAlterar }) {

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
            </td>
        </tr>
    )
}

export default ListarUtilizadoresCard