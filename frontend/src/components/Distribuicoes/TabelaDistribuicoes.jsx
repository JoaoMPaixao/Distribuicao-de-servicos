import ListarDistribuicoesCard from './ListarDistribuicoesCard.jsx'
import Pagination from '../Pagination.jsx'


function DistribuicoesTable({ distribuicoes, paginacao, porPagina, onMudarPorPagina, onVer, onComparar, onClonar, onDesativar, onAlterarEstado }) {

    return (
        <div className="table-card">
            <div className="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th className="sortable">Nome / Versão <span className="sort-icon"></span></th>
                        <th className="sortable">Descrição <span className="sort-icon"></span></th>
                        <th className="sortable">Ano Letivo <span className="sort-icon"></span></th>
                        <th className="sortable">Estado <span className="sort-icon"></span></th>
                        <th className="sortable">Data de Criação <span className="sort-icon"></span></th>
                        <th className="sortable">Ações <span className="sort-icon"></span></th>
                    </tr>
                    </thead>
                    <tbody>
                    {distribuicoes.map((d) => (
                        <ListarDistribuicoesCard
                            key={d.id}
                            distribuicao={d}
                            onVer={onVer}
                            onComparar={onComparar}
                            onClonar={onClonar}
                            onDesativar={onDesativar}
                            onAlterarEstado={onAlterarEstado}
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

export default DistribuicoesTable