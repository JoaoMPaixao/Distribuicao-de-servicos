function FilterBarDepartamentos({ pesquisa, setPesquisa}) {
    return (
        <div className="filter-bar">
            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar departamentos…"
                    value={pesquisa}
                    onChange={(e) => setPesquisa(e.target.value)}
                />
            </div>

        </div>

    )
}

export default FilterBarDepartamentos