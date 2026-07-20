import Pagination from '../Pagination.jsx'
import ListarDepartamentosCard from "./ListarDepartamentosCard.jsx";


function DepartamentosTable({ departamentos, paginacao, porPagina, onMudarPorPagina, onAlterar, onApagar}) {

    return (
        <div className="table-card">
            <div className="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th className="sortable">Nome<span className="sort-icon"></span></th>
                        <th className="sortable">Coordenador <span className="sort-icon"></span></th>
                        <th className="sortable">Ações <span className="sort-icon"></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    {departamentos.map((d) => (
                        <ListarDepartamentosCard
                            key={d.id}
                            departamento={d}
                            onAlterar={onAlterar}
                            onApagar={onApagar}
                        />
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="table-footer">
        <span className="footer-info">
          A mostrar {paginacao.inicio} a {paginacao.fim} de <strong>{paginacao.total}</strong> resultados
        </span>
                <div className="select-wrap" style={{ display: 'inline-flex', alignItems: 'center', gap: '8px' }}>
                    <span className="footer-info">Por Página   </span>
                    <select
                        value={porPagina}
                        onChange={(e) => onMudarPorPagina(Number(e.target.value))}
                    >
                        <option value={5}>5 registos</option>
                        <option value={10}>10 registos</option>
                        <option value={15}>15 registos</option>
                        <option value={20}>20 registos</option>
                    </select>
                </div>
                <Pagination paginaAtual={paginacao.atual} totalPaginas={paginacao.totalPaginas} onMudarPagina={paginacao.onMudar} />
            </div>
        </div>
    )
}

export default DepartamentosTable