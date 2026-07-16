import { useState } from "react";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarUcsCard({ uc,  onAlterar, onUcTipoAula }) {



    return (
        <tr>
            <td className="cell-name">
                <div className="title">{uc.nome}</div>
            </td>
            <td>
                <h6>{uc.codigo_uc}</h6>
            </td>
            <td>
                <h6>{uc.semestre}</h6>
            </td>
            <td>
                <h6>{uc.ano_curricular}</h6>
            </td>
            <td>
                <h6>{uc.ects}</h6>
            </td>
            <td className="cell-name">
                {uc.horas}
            </td>
            <td>
                <span className={`badge ${badgeClasses[uc.estado]}`}>
                    {uc.estado}
                </span>
            </td>
            <td className="cell-name">{uc.departamento_nome}</td>
            <td className="cell-name">{uc.curso_nome}</td>
            <td className="actions-cell">
                <button className="icon-btn" title="Editar" onClick={() => onAlterar(uc)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>

                <button className="icon-btn" title="Tipos Aulas" onClick={() => onUcTipoAula(uc)}>
                    <i className="bi bi-list icon-icon"></i>
                </button>
            </td>
            
        </tr>
    )
}

export default ListarUcsCard