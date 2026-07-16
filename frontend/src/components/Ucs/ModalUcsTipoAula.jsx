import {useEffect, useState} from 'react'
import {getUcsTipoAulaByUc} from "../../api/ucsApi.jsx";
import {getTipoAula} from "../../api/ucsApi.jsx";
import {getCursos} from "../../api/cursosApi.jsx";

function ModalUcsTipoAula({ uc, onClose, onSubmit, tipoAulas }) {

    const [modoFormulario, setModoFormulario] = useState(false)
    const [ucTipoAula, setUcTipoAula] = useState(null)
    const [tipo_aula_id, setTipoAula] = useState('')
    const [horas, setHoras] = useState('')
    const [erros, setErros] = useState({})
    const [tiposDoUc, setTiposDoUc] = useState([])


    const carregarTipos = async () => {
        const response = await getUcsTipoAulaByUc(uc.id)
        if (response.success) {
            setTiposDoUc(response.data)
        } else {
        }
    }

    useEffect(() => {
        carregarTipos()
    }, [uc.id])

    const handleOverlayClick = (e) => {
        if (e.target === e.currentTarget) onClose()
    }

    const abrirNovo = () => {
        setUcTipoAula(null)
        setTipoAula('')
        setHoras('')
        setErros({})
        setModoFormulario(true)
    }

    const abrirEdicao = (ucTipoAula) => {
        setUcTipoAula(ucTipoAula)
        setTipoAula(ucTipoAula.tipo_aula_id || '')
        setHoras(ucTipoAula.horas || '')
        setErros({})
        setModoFormulario(true)
    }

    const cancelarFormulario = () => {
        setModoFormulario(false)
        setUcTipoAula(null)
        setErros({})
    }

    const handleSubmitForm = async () => {
        const novosErros = {}
        if (!tipo_aula_id) novosErros.tipo_aula = 'O tipo de aula é obrigatório.'
        if (!horas) novosErros.horas = 'As horas são obrigatórias.'

        if (Object.keys(novosErros).length > 0) {
            setErros(novosErros)
            return
        }

        if (ucTipoAula) {
            await onSubmit(ucTipoAula.id, { tipo_aula_id, horas, uc_id: uc.id })
        } else {
            await onSubmit({ tipo_aula_id, horas, uc_id: uc.id })
        }


        setModoFormulario(false)
        setUcTipoAula(null)
        carregarTipos()
    }

    return (
        <div className="modal-overlay" onClick={handleOverlayClick}>
            <div className="modal" role="dialog" aria-modal="true">

                <div className="modal-header">
                    <div className="modal-header-left">
                        <div className="modal-icon">
                            <i className="bi bi-list modal-btn-confirm_icon"></i>
                        </div>
                        <div>
                            <h2 className="modal-title">Tipos de Aula</h2>
                            <p className="modal-subtitle">
                                {uc.nome} — Gerir os tipos de aula e respetivas horas.
                            </p>
                        </div>
                    </div>
                    <button className="modal-close" onClick={onClose} title="Fechar">
                        <i className="bi bi-x"></i>
                    </button>
                </div>

                <div className="modal-body">
                    {!modoFormulario && (
                        <>
                            <div className="table-wrap">
                                <table>
                                    <thead>
                                    <tr>
                                        <th>Tipo de Aula</th>
                                        <th>Horas</th>
                                        <th>Ações</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {tiposDoUc.length === 0 && (
                                        <tr>
                                            <td colSpan={3}>Sem tipos de aula registados.</td>
                                        </tr>
                                    )}
                                    {tiposDoUc.map((d) => (
                                        <tr key={d.id}>
                                            <td>{d.tipo_aula_nome}</td>
                                            <td>{d.horas}</td>
                                            <td className="actions-cell">
                                                <button className="icon-btn" title="Editar" onClick={() => abrirEdicao(d)}>
                                                    <i className="bi bi-pencil icon-icon"></i>
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>

                            <button className="modal-btn-confirm" style={{ marginTop: '16px' }} onClick={abrirNovo}>
                                <i className="bi bi-plus modal-btn-confirm_icon"></i>
                                Adicionar Tipo de Aula
                            </button>
                        </>
                    )}

                    {modoFormulario && (
                        <div className="modal-row two-col">
                            <div className="modal-field">
                                <label className="modal-label" htmlFor="mTipoAula">Tipo de Aula <span className="required">*</span></label>
                                <div className="modal-input-wrap">
                                    <select className="modal-input modal-select" id="mTipoAula" value={tipo_aula_id} onChange={(e) => setTipoAula(e.target.value)}>
                                        <option value="" disabled hidden>Selecione um tipo de aula...</option>
                                        {tipoAulas.map((d) => (

                                            <option key = {d.id} value = {d.id}>
                                                {d.nome}
                                            </option>

                                        ))}
                                    </select>
                                </div>
                                {erros.tipo_aula_id && <span className="modal-error">{erros.tipo_aula_id}</span>}
                            </div>

                            <div className="modal-field">
                                <label className="modal-label" htmlFor="mHorasTipo">Horas <span className="required">*</span></label>
                                <div className="modal-input-wrap">
                                    <input
                                        className="modal-input modal-mono"
                                        id="mHorasTipo"
                                        type="text"
                                        value={horas}
                                        onChange={(e) => setHoras(e.target.value)}
                                    />
                                </div>
                                {erros.horas && <span className="modal-error">{erros.horas}</span>}
                            </div>
                        </div>
                    )}
                </div>

                <div className="modal-footer">
                    {modoFormulario ? (
                        <>
                            <button className="modal-btn-cancel" onClick={cancelarFormulario}>Voltar</button>
                            <button className="modal-btn-confirm" onClick={handleSubmitForm}>
                                <i className="bi bi-plus modal-btn-confirm_icon"></i>
                                {ucTipoAula ? 'Salvar alterações' : 'Criar'}
                            </button>
                        </>
                    ) : (
                        <button className="modal-btn-cancel" onClick={onClose}>Fechar</button>
                    )}
                </div>

            </div>
        </div>
    )
}

export default ModalUcsTipoAula