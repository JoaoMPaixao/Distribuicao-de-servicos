function FilterBarAtribuicoes({ anoCurricular, setAnoCurricular, curso, setCurso, semestre, setSemestre, onLimpar, cursos }) {
    return (
        <div className="filter-bar">
            <div className="select-wrap">
                <label>Curso</label>
                <select value={curso} onChange={(e) => setCurso(e.target.value)}>
                    <option value="">Todos os Cursos</option>
                    {cursos.map(d => (
                        <option key={d.id} value={d.id}>{d.nome}</option>
                    ))}
                </select>
            </div>

            <div className="select-wrap">
                <label>Ano curricular</label>
                <select value={anoCurricular} onChange={(e) => setAnoCurricular(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="1">1ºAno Curricular</option>
                    <option value="2">2ºAno Curricular</option>
                    <option value="3">3ºAno Curricular</option>
                </select>
            </div>

            <div className="select-wrap">
                <label>Semestre</label>
                <select value={semestre} onChange={(e) => setSemestre(e.target.value)}>
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