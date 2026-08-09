import { useState } from 'react'

function ModalDocente({ docente = null, onClose, onSubmit, departamentos }) {
    const [nome, setNome] = useState(docente?.nome || '')
    const [email, setEmail] = useState(docente?.email || '')
    const [estado, setEstado] = useState(docente?.estado || '')
    const [departamentoId, setDepartamento] = useState(docente?.departamentoId || '')
    const [erros, setErros] = useState({})

    //verifica se o docente é null, caso seja null significa que devemos criar, caso nao seja recebemos um docente ao qual deve ser editado
    const isEdicao = docente !== null

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'
        if (!estado.trim()) novosErros.estado = 'O estado é obrigatório.'
        if (!departamentoId) novosErros.departamento = 'O departamento é obrigatório.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(docente.id, { nome, email, estado, departamentoId })
        } else {
            onSubmit({ nome, email, estado, departamentoId })
        }
    }


    return (
        <div className="modal-overlay "  onClick={handleOverlayClick}>
            <div className="modal" role="dialog" aria-modal="true">

                <div className="modal-header">
                    <div className="modal-header-left">
                        <div className="modal-icon">
                            <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        </div>
                        <div>
                            <h2 className="modal-title">
                                {isEdicao ? 'Editar Docente' : 'Criar Docente'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o docente.'
                                    : 'Preencha os dados para criar um novo docente.'}
                            </p>
                        </div>
                    </div>
                    <button className="modal-close" onClick={onClose} title="Fechar">
                        <i className="bi bi-x"></i>
                    </button>
                </div>

                <div className="modal-body">
                    <div className="modal-row two-col">
                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mNome">Nome <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input"
                                    id="mNome"
                                    type="text"
                                    placeholder=""
                                    value={nome}
                                    onChange={(e) => setNome(e.target.value)}
                                />
                            </div>
                            {erros.nome && <span className="modal-error">{erros.nome}</span>}
                        </div>

                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mEmail">Email <span className="modal-label-optional">Opcional</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mEmail"
                                    type="text"
                                    placeholder="Ex: ddddd@ipportalegre.pt"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                            {erros.email && <span className="modal-error">{erros.email}</span>}
                        </div>
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mEstado">Estado <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mEstado" value={estado} onChange={(e) => setEstado(e.target.value)}>
                                <option value="" disabled hidden>Selecione um estado...</option>
                                <option value="ATIVO">ATIVO</option>
                                <option value="INATIVO">INATIVO</option>
                            </select>
                        </div>
                        {erros.estado && <span className="modal-error">{erros.estado}</span>}
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mDepartamento">Departamento <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mDepartamento" value={departamentoId} onChange={(e) => setDepartamento(e.target.value)}>
                                <option value="" disabled hidden>Selecione um departamento...</option>
                                {departamentos.map((d) => (

                                    <option key = {d.id} value = {d.id}>
                                        {d.nome}
                                    </option>

                                ))}
                            </select>
                        </div>
                        {erros.departamentoId && <span className="modal-error">{erros.departamentoId}</span>}
                    </div>

                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar docente'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalDocente