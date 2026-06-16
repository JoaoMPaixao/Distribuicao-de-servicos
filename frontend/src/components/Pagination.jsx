function Pagination({ paginaAtual, totalPaginas, onMudarPagina }) {
    // Criamos o array com base no total de páginas reais
    const paginas = Array.from({ length: totalPaginas }, (_, i) => i + 1)

    return (
        <div className="pagination">
            {/* Primeira página agora é 1 */}
            <button className="page-btn" onClick={() => onMudarPagina(1)} disabled={paginaAtual === 1}>
                <i className="bi bi-chevron-double-left"></i>
            </button>
            {/* Retroceder para o número anterior */}
            <button className="page-btn" onClick={() => onMudarPagina(paginaAtual - 1)} disabled={paginaAtual === 1}>
                <i className="bi bi-chevron-left"></i>
            </button>

            {paginas.map((p) => (
                <button
                    key={p}
                    className={`page-btn ${p === paginaAtual ? 'active' : ''}`}
                    onClick={() => onMudarPagina(p)}
                >
                    {p} {/* Mostra o valor real correto */}
                </button>
            ))}

            {/* Avançar com base no limite real do total */}
            <button className="page-btn" onClick={() => onMudarPagina(paginaAtual + 1)} disabled={paginaAtual >= totalPaginas}>
                <i className="bi bi-chevron-right"></i>
            </button>
            {/* Última página é o próprio total */}
            <button className="page-btn" onClick={() => onMudarPagina(totalPaginas)} disabled={paginaAtual >= totalPaginas}>
                <i className="bi bi-chevron-double-right"></i>
            </button>
        </div>
    )
}export default Pagination