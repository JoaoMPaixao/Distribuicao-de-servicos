import { useState } from "react";
import ModalConfirmacao from "../ModalConfirmacao.jsx";


const badgeClasses = {
    ATIVO: 'badge-aberto',
    INATIVO: 'badge-fechado',

}

function ListarCursosCard({ curso,  onAlterar, onApagar }) {

    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

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
                <button className="icon-btn" title="Editar" onClick={() => onAlterar(curso)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
                <button className="icon-btn" title="Apagar" onClick={() => setConfirmarDesativar(true)}>
                    <i className="bi bi-trash icon-icon"></i>
                </button>
                {confirmarDesativar && (
                    <ModalConfirmacao
                        mensagem={`Tem a certeza que deseja apagar "${curso.nome}"?`}
                        onConfirmar={() => { onApagar(curso.id); setConfirmarDesativar(false) }}
                        onCancelar={() => setConfirmarDesativar(false)}
                    />
                )}
            </td>

        </tr>
    )
}

export default ListarCursosCard