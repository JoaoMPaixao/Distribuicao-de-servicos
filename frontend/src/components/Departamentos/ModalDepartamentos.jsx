import { useState } from 'react'

function ModalDepartamento({ departamento = null, onClose, onSubmit}) {
    const [nome, setNome] = useState(departamento?.nome || '')
    const [coordenador, setCoordenador] = useState(departamento?.coordenador || '')
    const [erros, setErros] = useState({})

    //verifica se o departamento é null, caso seja null significa que devemos criar, caso nao seja recebemos um departamento ao qual deve ser editado
    const isEdicao = departamento !== null

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(departamento.id, { nome, coordenador})
        } else {
            onSubmit({ nome, coordenador})
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
                                {isEdicao ? 'Editar Departamento' : 'Criar Departamento'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o departamento.'
                                    : 'Preencha os dados para criar um novo departamento.'}
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
                            <label className="modal-label" htmlFor="mCoordenador">Coordenador <span className="modal-label-optional">Opcional</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mCoordenador"
                                    type="text"
                                    placeholder=""
                                    value={coordenador}
                                    onChange={(e) => setCoordenador(e.target.value)}
                                />
                            </div>
                            {erros.coordenador && <span className="modal-error">{erros.coordenador}</span>}
                        </div>
                    </div>
                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar departamento'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalDepartamento