// DocItem.jsx
function DocItem({ docente }) {
    const percentagem = Math.round((docente.horasAtribuidas / docente.horasTotal) * 100)
    const estaQuase = percentagem >= 80

    const handleDragStart = (e) => {
        e.dataTransfer.setData('docente', JSON.stringify({
            id: docente.id,
            nome: docente.nome
        }))
    }

    return (
        <div
            className="doc-item"
            draggable
            onDragStart={handleDragStart}
        >
            <div style={{ flex: 1, minWidth: 0 }}>
                <div className="doc-name">{docente.nome}</div>
                <div className="doc-dept">{docente.departamento_nome}</div>
                <div className="carga-bar-wrap">
                    <div
                        className={`carga-fill ${estaQuase ? 'fill-warn' : 'fill-ok'}`}
                        style={{ width: `${percentagem}%` }}
                    />
                </div>
            </div>
            <span className={`horas-badge ${estaQuase ? 'warn' : 'ok'}`}>
        {2}h/{12}h
      </span>
        </div>
    )
}

export default DocItem