function FilterBarUcs({ pesquisa, setPesquisa, departamentoFiltro, setDepartamentoFiltro, departamentos, cursoFiltro, setCursoFiltro, cursos }) {
    return (
        <div className="filter-bar">
            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar cursos…"
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
                <label>Curso</label>
                <select value={cursoFiltro} onChange={(e) => setCursoFiltro(e.target.value)}>
                    <option value="">Todos os Cursos</option>
                    {cursos.map(d => (
                        <option key={d.id} value={d.id}>{d.nome}</option>
                    ))}
                </select>
            </div>

        </div>


    )
}

export default FilterBarUcs