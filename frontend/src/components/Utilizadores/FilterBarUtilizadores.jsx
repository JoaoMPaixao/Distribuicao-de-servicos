function FilterBarUtilizadores({ pesquisa, setPesquisa, departamentoFiltro, setDepartamentoFiltro, departamentos, cargoFiltro, setCargoFiltro, cargos }) {
    return (
        <div className="filter-bar">
            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar utilizadores…"
                    value={pesquisa}
                    onChange={(e) => setPesquisa(e.target.value)}
                />
            </div>

            <div className="select-wrap">
                <label>Departamento</label>
                <select value={departamentoFiltro} onChange={(e) => setDepartamentoFiltro(e.target.value)}>
                    <option value="">Todos os Departamentos</option>
                    {departamentos.map(d => (
                        <option key={d.id} value={d.id}>{d.nome}</option>
                    ))}
                </select>
            </div>

            <div className="select-wrap">
                <label>Cargo</label>
                <select value={cargoFiltro} onChange={(e) => setCargoFiltro(e.target.value)}>
                    <option value="">Todos os Cargos</option>
                    {cargos.map(d => (
                        <option key={d.id} value={d.id}>{d.tipo}</option>
                    ))}
                </select>
            </div>

        </div>


    )
}

export default FilterBarUtilizadores