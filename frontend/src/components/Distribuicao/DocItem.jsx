// DocItem.jsx
function DocItem({ docente }) {
    const percentagem = Math.round((docente.horas / 12) * 100)
    const excedeu = percentagem > 100
    const estaQuase = percentagem >= 80 && percentagem <= 100

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
                        className={`carga-fill ${excedeu ? 'fill-danger' : estaQuase ? 'fill-warn' : 'fill-ok'}`}
                        style={{ width: `${percentagem}%` }}

                    />
                </div>
                <span style={{ fontSize: '12px', fontWeight: 'bold', minWidth: '35px' }}>
                        {percentagem}%
                    </span>
            </div>
            <span className={`horas-badge ${excedeu ? 'danger' : estaQuase ? 'warn' : 'ok'}`}>
        {docente.horas}h/{12}h
      </span>
        </div>
    )
}

export default DocItem