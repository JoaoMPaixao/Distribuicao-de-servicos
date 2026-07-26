function FilterBarAtribuicoes({ anoCurricularFiltro, setAnoCurricularFiltro, cursoFiltro, setCursoFiltro, docenteFiltro, setDocenteFiltro, semestreFiltro, setSemestreFiltro, onLimpar, cursos }) {
    return (

        <div className="filter-bar">

            <div className="search-wrap">
                <i className="bi bi-search search-icon"></i>
                <input
                    className="search-input"
                    type="text"
                    placeholder="Pesquisar docentes…"
                    value={docenteFiltro}
                    onChange={(e) => setDocenteFiltro(e.target.value)}
                />
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

            <div className="select-wrap">
                <label>Ano curricular</label>
                <select value={anoCurricularFiltro} onChange={(e) => setAnoCurricularFiltro(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="1">1ºAno Curricular</option>
                    <option value="2">2ºAno Curricular</option>
                    <option value="3">3ºAno Curricular</option>
                </select>
            </div>

            <div className="select-wrap">
                <label>Semestre</label>
                <select value={semestreFiltro} onChange={(e) => setSemestreFiltro(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="1">1ºSemestre</option>
                    <option value="2">2ºSemestre</option>
                </select>
            </div>

            <button className="btn-clear" onClick={onLimpar}>
                <i className="bi bi-x"></i>
                Limpar Filtros
            </button>

        </div>


    )
}

export default FilterBarAtribuicoes