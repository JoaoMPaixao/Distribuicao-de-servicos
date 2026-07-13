import { useState } from "react";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarCursosCard({ curso,  onAlterar }) {
    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

    console.log(curso)
    return (
        <tr>
            <td className="cell-name">
                <div className="title">{curso.nome}</div>
            </td>
            <td>
                <h6>{curso.codigo_uc}</h6>
            </td>
            <td>
                <span className={`badge ${badgeClasses[curso.estado]}`}>
                    {curso.estado}
                </span>
            </td>
            <td className="cell-name">{curso.departamento_nome}</td>
            <td className="actions-cell">
                <button className="icon-btn" title="Ver/Editar" onClick={() => onAlterar(curso.id)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
            </td>
        </tr>
    )
}

export default ListarCursosCard