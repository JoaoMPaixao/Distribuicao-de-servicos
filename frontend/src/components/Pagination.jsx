function Pagination({ paginaAtual, totalPaginas, onMudarPagina }) {

    const gerarPaginas = () => {
        const paginas = []
        const delta = 2 // quantas páginas mostrar de cada lado da atual

        const inicio = Math.max(2, paginaAtual - delta)
        const fim = Math.min(totalPaginas - 1, paginaAtual + delta)

        // sempre mostra a primeira
        paginas.push(1)

        // "..." entre a primeira e o bloco central (se houver gap)
        if (inicio > 2) paginas.push('...')

        // bloco central (vizinhas da atual)
        for (let i = inicio; i <= fim; i++) {
            paginas.push(i)
        }

        // "..." entre o bloco central e a última (se houver gap)
        if (fim < totalPaginas - 1) paginas.push('...')

        // sempre mostra a última (se houver mais de 1 página)
        if (totalPaginas > 1) paginas.push(totalPaginas)

        return paginas
    }

    return (
        <div className="pagination">
            <button className="page-btn" onClick={() => onMudarPagina(1)} disabled={paginaAtual === 1}>
                <i className="bi bi-chevron-double-left"></i>
            </button>
            <button className="page-btn" onClick={() => onMudarPagina(paginaAtual - 1)} disabled={paginaAtual === 1}>
                <i className="bi bi-chevron-left"></i>
            </button>

            {gerarPaginas().map((p, index) =>
                p === '...'
                    ? (
                        <span key={`ellipsis-${index}`} className="page-btn" style={{ cursor: 'default' }}>
                            ...
                        </span>
                    ) : (
                        <button
                            key={p}
                            className={`page-btn ${p === paginaAtual ? 'active' : ''}`}
                            onClick={() => onMudarPagina(p)}
                        >
                            {p}
                        </button>
                    )
            )}

            <button className="page-btn" onClick={() => onMudarPagina(paginaAtual + 1)} disabled={paginaAtual >= totalPaginas}>
                <i className="bi bi-chevron-right"></i>
            </button>
            <button className="page-btn" onClick={() => onMudarPagina(totalPaginas)} disabled={paginaAtual >= totalPaginas}>
                <i className="bi bi-chevron-double-right"></i>
            </button>
        </div>
    )
}

export default Pagination