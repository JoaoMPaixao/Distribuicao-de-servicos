import { useState } from 'react'

function ModalUtilizador({ utilizador = null, onClose, onSubmit, departamentos, cargos }) {
    const [nome, setNome] = useState(utilizador?.nome || '')
    const [email, setEmail] = useState(utilizador?.email || '')
    const [password, setPassword] = useState(utilizador?.password || '')
    const [estado, setEstado] = useState(utilizador?.estado || '')
    const [departamento_id, setDepartamento] = useState(utilizador?.departamento_id || '')
    const [cargo_id, setCargo] = useState(utilizador?.cargo_id || '')
    const [erros, setErros] = useState({})

    //verifica se o utilizador é null, caso seja null significa que devemos criar, caso nao seja recebemos um utilizador ao qual deve ser editado
    const isEdicao = utilizador !== null

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'
        if (!email.trim()) novosErros.email = 'O email é obrigatório.'
        if (!password.trim()) novosErros.password = 'A password é obrigatório.'
        if (!estado.trim()) novosErros.estado = 'O estado é obrigatório.'
        if (!departamento_id) novosErros.departamento = 'O departamento é obrigatório.'
        if (!cargo_id) novosErros.cargo = 'O cargo é obrigatório.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(utilizador.id, { nome, email, password, estado, departamento_id, cargo_id })
        } else {
            onSubmit({nome, email, password, estado, departamento_id, cargo_id })
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
                                {isEdicao ? 'Editar Utilizador' : 'Criar Utilizador'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o utilizador.'
                                    : 'Preencha os dados para criar um novo utilizador.'}
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
                            <label className="modal-label" htmlFor="mEmail">Email <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mEmail"
                                    type="text"
                                    placeholder="xxx@ipportalegre.pt"
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                />
                            </div>
                            {erros.email && <span className="modal-error">{erros.email}</span>}
                        </div>

                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mPassword">Password <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mPassword"
                                    type="text"
                                    placeholder=""
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                />
                            </div>
                            {erros.password && <span className="modal-error">{erros.password}</span>}
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

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mCargo">Cargo <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mCargo" value={cargo_id} onChange={(e) => setCargo(e.target.value)}>
                                <option value="" disabled hidden>Selecione um cargo..</option>
                                {cargos.map((d) => (

                                    <option key = {d.id} value = {d.id}>
                                        {d.tipo}
                                    </option>

                                ))}
                            </select>
                        </div>
                        {erros.cargo_id && <span className="modal-error">{erros.cargo_id}</span>}
                    </div>

                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar utilizador'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalUtilizador