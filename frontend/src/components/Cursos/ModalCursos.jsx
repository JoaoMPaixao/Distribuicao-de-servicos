import { useState } from 'react'

function ModalCurso({ curso = null, onClose, onSubmit, departamentos }) {
    const [nome, setNome] = useState(curso?.nome || '')
    const [codigo_uc, setCodigo_Uc] = useState(curso?.codigo_uc || '')
    const [estado, setEstado] = useState(curso?.estado || '')
    const [departamento_id, setDepartamento] = useState(curso?.departamento_id || '')
    const [erros, setErros] = useState({})

    //verifica se o curso é null, caso seja null significa que devemos criar, caso nao seja recebemos um curso ao qual deve ser editado
    const isEdicao = curso !== null

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'
        if (!codigo_uc.trim()) novosErros.codigo_uc = 'O codigo uc é obrigatório.'
        if (!estado.trim()) novosErros.estado = 'O estado é obrigatório.'
        if (!departamento_id) novosErros.departamento = 'O departamento é obrigatório.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(curso.id, { nome, codigo_uc, estado, departamento_id })
        } else {
            onSubmit({ nome, codigo_uc, estado, departamento_id })
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
                                {isEdicao ? 'Editar Curso' : 'Criar Curso'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o curso.'
                                    : 'Preencha os dados para criar um novo curso.'}
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
                            <label className="modal-label" htmlFor="mCodigo_uc">Codigo_uc <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mCodigo_uc"
                                    type="text"
                                    placeholder="Ex: 1543"
                                    value={codigo_uc}
                                    onChange={(e) => setCodigo_Uc(e.target.value)}
                                />
                            </div>
                            {erros.codigo_uc && <span className="modal-error">{erros.codigo_uc}</span>}
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
                            <select className="modal-input modal-select" id="mDepartamento" value={departamento_id} onChange={(e) => setDepartamento(e.target.value)}>
                                <option value="" disabled hidden>Selecione um departamento...</option>
                                {departamentos.map((d) => (

                                    <option key = {d.id} value = {d.id}>
                                        {d.nome}
                                    </option>

                                    ))}
                            </select>
                        </div>
                        {erros.departamento_id && <span className="modal-error">{erros.departamento_id}</span>}
                    </div>

                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar curso'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalCurso