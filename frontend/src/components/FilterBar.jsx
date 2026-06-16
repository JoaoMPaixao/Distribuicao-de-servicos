function FilterBar({ pesquisa, setPesquisa, anoLetivo, setAnoLetivo, estado, setEstado, onLimpar }) {
    return (
        <div className="filter-bar">
            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar distribuições…"
                    value={pesquisa}
                    onChange={(e) => setPesquisa(e.target.value)}
                />
            </div>

            <div className="select-wrap">
                <label>Ano Letivo</label>
                <select value={anoLetivo} onChange={(e) => setAnoLetivo(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="2026/2027">2026/2027</option>
                    <option value="2025/2026">2025/2026</option>
                    <option value="2024/2025">2024/2025</option>
                </select>
            </div>

            <div className="select-wrap">
                <label>Estado</label>
                <select value={estado} onChange={(e) => setEstado(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="ABERTO">Aberto</option>
                    <option value="FECHADO">Fechado</option>
                    <option value="APROVADO">Aprovado</option>
                    <option value="RECUSADO">Recusado</option>
                    <option value="FINAL">Final</option>
                </select>
            </div>

            <button className="btn-clear" onClick={onLimpar}>
                <i className="bi bi-x"></i>
                Limpar Filtros
            </button>
        </div>
    )
}

export default FilterBar