import { useState } from 'react'

function ModalUc({ uc = null, onClose, onSubmit, departamentos, cursos }) {
    const [nome, setNome] = useState(uc?.nome || '')
    const [codigo_uc, setCodigo_Uc] = useState(uc?.codigo_uc || '')
    const [semestre, setSemestre] = useState(uc?.semestre || '')
    const [ano_curricular, setAno_Curricular] = useState(uc?.ano_curricular || '')
    const [ects, setEcts] = useState(uc?.ects || '')
    const [horas, setHoras] = useState(uc?.horas || '')
    const [estado, setEstado] = useState(uc?.estado || '')
    const [departamento_id, setDepartamento] = useState(uc?.departamento_id || '')
    const [curso_id, setCurso] = useState(uc?.curso_id || '')
    const [erros, setErros] = useState({})

    //verifica se o uc é null, caso seja null significa que devemos criar, caso nao seja recebemos um uc ao qual deve ser editado
    const isEdicao = uc !== null

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const handleSubmit = () => {
        const novosErros = {}
        if (!nome.trim()) novosErros.nome = 'O nome é obrigatório.'
        if (!codigo_uc.trim()) novosErros.codigo_uc = 'O codigo uc é obrigatório.'
        if (!semestre) novosErros.semestre = 'O semestre é obrigatório.'
        if (!ano_curricular) novosErros.ano_curricular = 'O ano_curricular é obrigatório.'
        if (!ects) novosErros.ects = 'O ects é obrigatório.'
        if (!horas) novosErros.horas = 'O horas é obrigatório.'
        if (!estado.trim()) novosErros.estado = 'O estado é obrigatório.'
        if (!departamento_id) novosErros.departamento = 'O departamento é obrigatório.'
        if (!curso_id) novosErros.curso = 'O curso é obrigatório.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (isEdicao) {
            onSubmit(uc.id, { nome, codigo_uc, semestre, ano_curricular, ects, horas, estado, departamento_id, curso_id })
        } else {
            onSubmit({ nome, codigo_uc, semestre, ano_curricular, ects, horas, estado, departamento_id, curso_id  })
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
                                {isEdicao ? 'Editar Uc' : 'Criar Uc'}
                            </h2>
                            <p className="modal-subtitle">
                                {isEdicao
                                    ? 'Altere os dados necessários para atualizar o uc.'
                                    : 'Preencha os dados para criar um novo uc.'}
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

                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mEcts">Ects <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mEcts"
                                    type="text"
                                    placeholder="Ex: 7.5"
                                    value={ects}
                                    onChange={(e) => setEcts(e.target.value)}
                                />
                            </div>
                            {erros.ects && <span className="modal-error">{erros.ects}</span>}
                        </div>

                        <div className="modal-field">
                            <label className="modal-label" htmlFor="mHoras">Horas <span className="required">*</span></label>
                            <div className="modal-input-wrap">
                                <input
                                    className="modal-input modal-mono"
                                    id="mHoras"
                                    type="text"
                                    placeholder=""
                                    value={horas}
                                    onChange={(e) => setHoras(e.target.value)}
                                />
                            </div>
                            {erros.horas && <span className="modal-error">{erros.horas}</span>}
                        </div>
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mSemestre">Semestre <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mSemestre" value={semestre} onChange={(e) => setSemestre(e.target.value)}>
                                <option value="" disabled hidden>Selecione o semestre...</option>
                                <option value="1">1ºSemestre</option>
                                <option value="2">2ºSemestre</option>
                            </select>
                        </div>
                        {erros.semestre && <span className="modal-error">{erros.semestre}</span>}
                    </div>

                    <div className="modal-field">
                        <label className="modal-label" htmlFor="mAno_Curricular">Ano_Curricular <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mAno_Curricular" value={ano_curricular} onChange={(e) => setAno_Curricular(e.target.value)}>
                                <option value="" disabled hidden>Selecione um ano...</option>
                                <option value="1">1ºAno</option>
                                <option value="2">2ºAno</option>
                                <option value="3">3ºAno</option>
                            </select>
                        </div>
                        {erros.ano_curricular && <span className="modal-error">{erros.ano_curricular}</span>}
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
                        <label className="modal-label" htmlFor="mCurso">Curso <span className="required">*</span></label>
                        <div className="modal-input-wrap">
                            <select className="modal-input modal-select" id="mCurso" value={curso_id} onChange={(e) => setCurso(e.target.value)}>
                                <option value="" disabled hidden>Selecione um curso...</option>
                                {cursos.map((d) => (

                                    <option key = {d.id} value = {d.id}>
                                        {d.nome}
                                    </option>

                                ))}
                            </select>
                        </div>
                        {erros.curso_id && <span className="modal-error">{erros.curso_id}</span>}
                    </div>

                </div>

                <div className="modal-footer">
                    <button className="modal-btn-cancel" onClick={onClose}>Cancelar</button>
                    <button className="modal-btn-confirm" onClick={handleSubmit}>
                        <i className="bi bi-plus modal-btn-confirm_icon"></i>
                        {isEdicao ? 'Salvar alterações' : 'Criar uc'}
                    </button>
                </div>

            </div>
        </div>
    )
}

export default ModalUc