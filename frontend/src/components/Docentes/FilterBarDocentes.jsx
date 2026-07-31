function FilterBarDocentes({ pesquisa, setPesquisa, departamentoFiltro, setDepartamentoFiltro, departamentos }) {
    return (
        <div className="filter-bar">
            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar docentes…"
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

        </div>


    )
}

export default FilterBarDocentes