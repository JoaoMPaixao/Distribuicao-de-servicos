import ModalConfirmacao from "../ModalConfirmacao.jsx";
import {useState} from "react";

function ListarDepartamentosCard({ departamento,  onAlterar, onApagar }) {
    const [confirmarDesativar, setConfirmarDesativar] = useState(false)

    return (
        <tr>
            <td className="cell-name">
                <div className="title">{departamento.nome}</div>
            </td>
            <td>
                <h6>{departamento.coordenador}</h6>
            </td>
            <td className="actions-cell">
                <button className="icon-btn" title="Editar" onClick={() => onAlterar(departamento)}>
                    <i className="bi bi-eye icon-icon"></i>
                </button>
                <button className="icon-btn" title="Apagar" onClick={() => setConfirmarDesativar(true)}>
                    <i className="bi bi-trash icon-icon"></i>
                </button>
                {confirmarDesativar && (
                    <ModalConfirmacao
                        mensagem={`Tem a certeza que deseja apagar "${departamento.nome}"?`}
                        onConfirmar={() => { onApagar(departamento.id); setConfirmarDesativar(false) }}
                        onCancelar={() => setConfirmarDesativar(false)}
                    />
                )}
            </td>
        </tr>
    )
}

export default ListarDepartamentosCard