
function ListarDepartamentosCard({ departamento,  onAlterar }) {


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
            </td>
        </tr>
    )
}

export default ListarDepartamentosCard